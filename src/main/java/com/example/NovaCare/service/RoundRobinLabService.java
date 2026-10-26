package com.example.NovaCare.service;

import com.example.NovaCare.model.LabTest;
import com.example.NovaCare.repository.LabTestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Service
public class RoundRobinLabService {

    @Autowired
    private LabTestRepository labTestRepository;

    private final int TIME_QUANTUM = 5;
    private Queue<LabTest> roundRobinQueue = new LinkedList<>();
    private LabTest currentTest = null;

    public LabTest addLabTest(LabTest test) {
        test.setStatus("WAITING");
        test.setRemainingTime(test.getEstimatedDuration());
        LabTest saved = labTestRepository.save(test);
        refreshQueue();
        startNextIfAvailable();
        return saved;
    }

    private void refreshQueue() {
        List<LabTest> waitingTests = labTestRepository.findByStatus("WAITING");
        roundRobinQueue.clear();
        roundRobinQueue.addAll(waitingTests);

        int cumulativeTime = 0;
        for (LabTest test : waitingTests) {
            test.setWaitingTime(cumulativeTime);
            cumulativeTime += Math.min(test.getRemainingTime(), TIME_QUANTUM);
            labTestRepository.save(test);
        }
    }

    private void startNextIfAvailable() {
        if (currentTest == null && !roundRobinQueue.isEmpty()) {
            currentTest = roundRobinQueue.poll();
            currentTest.setStatus("RUNNING");
            currentTest.setStartTime(LocalDateTime.now());
            labTestRepository.save(currentTest);
        }
    }

    public LabTest executeQuantum() {
        if (currentTest == null) {
            startNextIfAvailable();
            if (currentTest == null) return null;
        }

        int remaining = currentTest.getRemainingTime();

        if (remaining > TIME_QUANTUM) {
            currentTest.setRemainingTime(remaining - TIME_QUANTUM);
            currentTest.setStatus("WAITING");
            currentTest.setCompletedQuantums(currentTest.getCompletedQuantums() + 1);
            labTestRepository.save(currentTest);
            roundRobinQueue.add(currentTest);
            currentTest = null;
            startNextIfAvailable();
            return null;
        } else {
            currentTest.setRemainingTime(0);
            currentTest.setStatus("COMPLETED");
            currentTest.setEndTime(LocalDateTime.now());
            currentTest.setCompletedQuantums(currentTest.getCompletedQuantums() + 1);
            labTestRepository.save(currentTest);
            LabTest completed = currentTest;
            currentTest = null;
            startNextIfAvailable();
            return completed;
        }
    }

    public void executeAllQuantums() {
        int maxIterations = 1000;
        int iterations = 0;
        while ((currentTest != null || !roundRobinQueue.isEmpty()) && iterations < maxIterations) {
            executeQuantum();
            iterations++;
        }
    }

    public List<LabTest> getAllTests() {
        return labTestRepository.findAll();
    }

    public LabTest getCurrentTest() {
        if (currentTest != null) return currentTest;
        List<LabTest> running = labTestRepository.findByStatus("RUNNING");
        if (!running.isEmpty()) {
            currentTest = running.get(0);
            return currentTest;
        }
        return null;
    }

    public List<LabTest> getWaitingQueue() {
        return labTestRepository.findByStatus("WAITING");
    }

    public List<LabTest> getCompletedTests() {
        return labTestRepository.findByStatus("COMPLETED");
    }

    public List<LabTest> getRoundRobinOrder() {
        refreshQueue();
        return new LinkedList<>(roundRobinQueue);
    }

    public void clearAllTests() {
        labTestRepository.deleteAll();
        roundRobinQueue.clear();
        currentTest = null;
    }

    public void clearCompletedTests() {
        labTestRepository.deleteByStatus("COMPLETED");
    }
}