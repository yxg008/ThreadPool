# Java Thread Pool Implementation

## Overview

This project is a simple Java thread pool implementation. It consists of three main classes:

1. `Main`: Demonstrates how to use the ThreadPool class.
2. `Task`: Represents a task to be executed. Implements `Runnable`.
3. `ThreadPool`: Manages a pool of threads and a task queue.

## Features

- Create a thread pool with a specified number of threads.
- Add tasks to a task queue.
- Each worker thread picks up a task from the queue and executes it.
- Graceful shutdown of the thread pool.

## Classes

### Main

Contains the `main` method to start the application. Initializes a `ThreadPool` with 7 worker threads and adds 5 `Task` objects to the pool for execution. Finally, it shuts down the thread pool.

### Task

Implements the `Runnable` interface. Each task object is given a unique task number for identification. The `run` method simply prints out the task number.

### ThreadPool

Manages a specified number of worker threads and a `LinkedBlockingQueue` to hold tasks. It has methods for adding tasks (`add`) and shutting down the thread pool (`shutdown`).

## Usage

Compile all the Java files and run the `Main` class:

```bash
javac Main.java Task.java ThreadPool.java
java Main
