package net.toadless.radio.objects.module;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import net.toadless.radio.Radio;
import net.toadless.radio.objects.other.Task;
import net.toadless.radio.objects.exception.ModuleNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Modules
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Modules.class);
    public static final String MODULE_PACKAGE = "net.toadless.radio.modules";

    private final ClassGraph classGraph = new ClassGraph().acceptPackages(MODULE_PACKAGE);
    private final Map<Class<?>, Module> modules;
    private final Radio radio;

    // Task handling
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private final List<Task> tasks = new ArrayList<>();
    private final List<UUID> currentUUIDs = new ArrayList<>();

    public Modules(Radio radio)
    {
        this.radio = radio;
        modules = loadModules();
    }

    public Map<Class<?>, Module> loadModules()
    {
        Map<Class<?>, Module> modules = new LinkedHashMap<>();
        try (ScanResult result = classGraph.scan())
        {
            for (ClassInfo cls : result.getAllClasses())
            {
                Constructor<?>[] constructors = cls.loadClass().getDeclaredConstructors();
                if (constructors.length == 0)
                {
                    radio.getLogger().warn("No valid constructors found for Module class (" + cls.getSimpleName() + ")!");
                    continue;
                }
                if (constructors[0].getParameterCount() > 2)
                {
                    continue;
                }
                Object instance = constructors[0].newInstance(radio, this);
                if (!(instance instanceof Module))
                {
                    radio.getLogger().warn("Non Module class (" + cls.getSimpleName() + ") found in commands package!");
                    continue;
                }
                modules.put(instance.getClass(), ((Module) instance));
            }
        }
        catch (Exception exception)
        {
            radio.getLogger().error("A module exception occurred", exception);
            System.exit(1);
        }

        radio.getLogger().info("Finished loading {} modules", modules.size());
        return modules;
    }

    public Object[] getModules()
    {
        return this.modules.values().toArray();
    }

    @SuppressWarnings("unchecked")
    public <T extends Module> T get(Class<T> clazz)
    {
        var module = this.modules.get(clazz);
        if(module == null)
        {
            throw new ModuleNotFoundException(clazz);
        }
        return (T) module;
    }

    // Task handling
    public Task addTask(Runnable task, TimeUnit unit, long time)
    {
        String taskName = getTaskName();
        Task Task = new Task(scheduler.schedule(task, time, unit), taskName, time, unit);
        tasks.add(Task);
        scheduleDeletion(Task);
        LOGGER.debug("Added new task with name " + taskName + " expires in " + Task.getExpiresAt() + " " + Task.getUnit());
        return Task;
    }

    public Task addTask(Runnable task, String taskName, TimeUnit unit, long time)
    {
        Task Task = new Task(scheduler.schedule(task, time, unit), taskName, time, unit);
        tasks.add(Task);
        scheduleDeletion(Task);
        LOGGER.debug("Added new task with name " + taskName + " expires in " + Task.getExpiresAt() + " " + Task.getUnit());
        return Task;
    }

    public Task addTask(Callable<?> task, String taskName, TimeUnit unit, long time)
    {
        Task Task = new Task(scheduler.schedule(task, time, unit), taskName, time, unit);
        tasks.add(Task);
        scheduleDeletion(Task);
        LOGGER.debug("Added new task with name " + taskName + " expires in " + Task.getExpiresAt() + " " + Task.getUnit());
        return Task;
    }

    public Task addRepeatingTask(Runnable task, String taskName, long initialDelay, TimeUnit unit, long period)
    {
        Task Task = new Task(scheduler.scheduleAtFixedRate(task, initialDelay, period, unit), taskName, period + initialDelay, unit);
        tasks.add(Task);
        return Task;
    }

    public Task addRepeatingTask(Runnable task, String taskName, TimeUnit unit, long time)
    {
        return addRepeatingTask(task, taskName, 0, unit, time);
    }

    public Task addRepeatingTask(Runnable task, TimeUnit unit, long time)
    {
        return addRepeatingTask(task, "" + System.currentTimeMillis(), 0, unit, time);
    }

    public Task getTask(String taskName)
    {
        for (Task task : tasks)
        {
            if (task.getName().equalsIgnoreCase(taskName))
            {
                return task;
            }
        }
        return null;
    }

    public void cancelTask(String taskName, boolean shouldInterrupt)
    {
        LOGGER.debug("Cancelling task " + taskName);
        for (Task task : tasks)
        {
            if (task.getName().equalsIgnoreCase(taskName))
            {
                LOGGER.debug("Cancelled task " + taskName);
                task.getTask().cancel(shouldInterrupt);
                return;
            }
        }
        LOGGER.debug("Task " + taskName + " could not be found");
    }

    public void close()
    {
        LOGGER.debug("Closing TaskHandler");
        for (Task task : tasks)
        {
            task.cancel(false);
        }
        LOGGER.debug("TaskHandler closed");
    }

    public String getTaskName()
    {
        UUID uuid = UUID.randomUUID();
        if (!currentUUIDs.contains(uuid))
        {
            currentUUIDs.add(uuid);
            return uuid.toString();
        }
        else
        {
            return getTaskName();
        }
    }

    public List<Task> getTasks()
    {
        return tasks;
    }

    private void scheduleDeletion(Task task)
    {
        LOGGER.debug("Task " + task.getName() + " scheduled for deletion in " + task.getExpiresAt() + " " + task.getUnit());
        scheduler.schedule(() -> tasks.remove(task), task.getExpiresAt(), task.getUnit());
    }
}