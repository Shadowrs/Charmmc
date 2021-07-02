package svenhjol.charm.loader;

import svenhjol.charm.Charm;
import svenhjol.charm.helper.ClassHelper;
import svenhjol.charm.helper.StringHelper;
import svenhjol.charm.module.core.Core;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public abstract class ModuleLoader<T extends CharmModule> {
    private static final List<String> MOD_IDS = new ArrayList<>();
    private final Map<String, T> MODULES = new TreeMap<>();
    private final String modId;
    private final String basePackage;

    public ModuleLoader(String modId, String basePackage) {
        this.modId = modId;
        this.basePackage = basePackage;

        MOD_IDS.add(modId);
    }

    public void init() {
        // do module registration
        this.register();

        // do module dependency checks
        this.dependencies();

        // do module tasks
        this.run();
    }

    protected void register() {
        prepareModules().forEach((id, module) -> {
            Core.debug("[ModuleLoader] Registering " + module.getName());
            MODULES.put(StringHelper.upperCamelToSnake(module.getName()), module);
            module.register();
        });
    }

    protected void dependencies() {
        getModules().forEach(module -> {
            boolean debug = Core.isDebugMode();
            boolean enabledInConfig = module.isEnabledInConfig();
            boolean passedDependencyCheck = module.getDependencies().isEmpty() || module.getDependencies().stream().allMatch(dep -> dep.test(module));
            module.setEnabled(enabledInConfig && passedDependencyCheck);

            if (!enabledInConfig) {
                if (debug) Charm.LOG.warn("[ModuleLoader] Disabled in configuration: " + module.getName());
            } else if (!passedDependencyCheck) {
                if (debug) Charm.LOG.warn("[ModuleLoader] Failed dependency check: " + module.getName());
            } else if (!module.isEnabled()) {
                if (debug) Charm.LOG.warn("[ModuleLoader] Disabled automatically: " + module.getName());
            } else {
                Charm.LOG.info("[ModuleLoader] Enabled " + module.getName());
            }
        });
    }

    protected void run() {
        getEnabledModules().forEach(module -> {
            Charm.LOG.info("[ModuleLoader] Running " + module.getName());
            module.runWhenEnabled();
        });

        getDisabledModules().forEach(module -> {
            Core.debug("[ModuleLoader] Running disabled tasks: " + module.getName());
            module.runWhenDisabled();
        });
    }

    /**
     * Use this anywhere to check a module's enabled status for any Charm-based module.
     */
    public boolean isEnabled(Class<? extends T> clazz) {
        return getModules().stream().anyMatch(module -> module.getClass().equals(clazz));
    }

    /**
     * Use this anywhere to check a module's enabled status for any Charm-based module.
     */
    public boolean isEnabled(String moduleName) {
        if (Core.isDebugMode() && moduleName.contains(":")) {
            // deprecated, warn about it
            Charm.LOG.warn("[ModuleLoader] Deprecated: Module `" + moduleName + "` no longer requires namespace");
            moduleName = moduleName.split(":")[1];
        }

        T module = getModule(moduleName);
        return module != null && module.isEnabled();
    }

    public List<T> getModules() {
        return MODULES.values().stream().toList();
    }

    public List<T> getEnabledModules() {
        return MODULES.values().stream().filter(CharmModule::isEnabled).collect(Collectors.toList());
    }

    public List<T> getDisabledModules() {
        return MODULES.values().stream().filter(m -> !m.isEnabled()).collect(Collectors.toList());
    }

    @Nullable
    public T getModule(String moduleName) {
        String name;

        // if fully qualified name, split
        if (moduleName.contains(".")) {
            String[] split = moduleName.split("\\.");
            name = split[split.length - 1];
        } else {
            name = moduleName;
        }

        String lower = StringHelper.upperCamelToSnake(name);
        return MODULES.getOrDefault(lower, null);
    }

    protected Map<String, T> prepareModules() {
        Map<String, T> discoveredModules = new LinkedHashMap<>();
        List<Class<T>> discoveredClasses = ClassHelper.getClassesInPackage(getBasePackage(), getModuleAnnotation());

        if (discoveredClasses.isEmpty())
            Charm.LOG.warn("[ModuleLoader] Seems no module classes were processed... this is probably bad.");

        Map<String, T> loaded = new TreeMap<>();
        for (Class<T> clazz : discoveredClasses) {
            try {
                T module = clazz.getDeclaredConstructor().newInstance();
                setupModuleAnnotations(clazz, module);

                String moduleName = module.getName();
                loaded.put(moduleName, module);
            } catch (Exception e) {
                Charm.LOG.error("[ModuleLoader] Error loading module " + clazz.toString() + ": " + e.getMessage());
            }
        }

        // defer module config to subclasses
        setupModuleConfig(new LinkedList<>(loaded.values()));

        // sort by module priority
        ArrayList<T> modList = new ArrayList<>(loaded.values());
        modList.sort((mod1, mod2) -> {
            if (mod1.getPriority() == mod2.getPriority()) {
                return mod1.getName().compareTo(mod2.getName()); // sort by name
            } else {
                return Integer.compare(mod2.getPriority(), mod1.getPriority()); // sort by priority
            }
        });

        // assemble loaded modules into discoveredModules and return
        for (T mod : modList) {
            for (Map.Entry<String, T> entry : loaded.entrySet()) {
                if (entry.getValue().equals(mod)) {
                    discoveredModules.put(entry.getKey(), mod);
                    break;
                }
            }
        }

        return discoveredModules;
    }

    protected String getModId() {
        return this.modId;
    }

    protected String getBasePackage() {
        return this.basePackage;
    }

    protected abstract String getModuleAnnotation();

    protected abstract void setupModuleAnnotations(Class<T> clazz, T module) throws IllegalStateException;

    protected void setupModuleConfig(List<T> modules) {
        // no op
    }

    public static List<String> getModIds() {
        return MOD_IDS;
    }
}
