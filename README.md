[![REUSE status](https://api.reuse.software/badge/github.com/SAP/neonbee-plugin-kickstart)](https://api.reuse.software/info/github.com/SAP/neonbee-plugin-kickstart)
# NeonBee Kickstart Gradle Plugin

The NeonBee Kickstart Gradle Plugin is a collection of several plugins that helps to develop
[NeonBee](https://github.com/SAP/neonbee) applications. The main focus of this plugin is to lower
the barrier to entry and give developers interested in NeonBee a kickstart to develop their first NeonBee applications.

- [Application Plugin](#application-plugin)
  - [Application Configuration](#application-configuration)
- [Module Plugin](#module-plugin)
  - [Module Configuration](#module-configuration)
- [Quality Plugin](#quality-plugin)
  - [Quality Configuration](#quality-configuration)
- [Models Plugin](#models-plugin)
- [Plugin Development](#plugin-development)
  - [Formatter](#formatter)
  - [Tests](#tests)

## Application Plugin
The *Application Plugin* helps to start NeonBee directly from your current project. It will automatically
scan the classpath of your project for `Deployables` and `Hooks` and deploys them.

By applying the *Application Plugin* it will also add the task `./gradlew initModels` to apply
the *Models Plugin*. This allows you to add `Models` which are required for `ÈntityVerticles`.

If the specified `working_dir` doesn't exist the *Application Plugin* will automatically create and
prefills it with the required structure and a default configuration.

By default, NeonBee listens to port `8080`. In case you favour another port you can specify it in the `ServerVerticle`
configuration (`${working_dir}/config/io.neonbee.internal.verticle.ServerVerticle.yaml`).

### Application Configuration

```groovy
neonbeeApplication {
    neonbeeVersion = '0.10.0'  // The NeonBee version
    workingDir = 'working_dir' // The working directory of NeonBee (Default: working_dir)
}
```

In case you want to add additional `NeonBee Modules`, you can do this by adding them to the `neonbeeModules`
configuration. All `NeonBee Modules` artifacts added to this configuration will be copied to the `verticles` directory
of your specified working directory.

```groovy
dependencies {
    neonbeeModules group: 'org.example', name: 'example-component-module', version: '1.0.0'
}
```

The plugin can be added with the following code to your `build.gradle` of the root project:

```groovy
plugins {
    id 'io.neonbee.gradle.kickstart.application' version '0.1.0'
}
```

## Module Plugin

The *Module Plugin* helps to build *NeonBee Modules*. A *NeonBee Module* is a JAR containing
`Deployables`, `Hooks` and `Models`.

⚠️ The *Module Plugin* doesn't support `Models` by default. In case your component requires `Models`,
apply the *Models Plugin* by triggering the task `./gradlew initModels`.

### Module Configuration

The *Module Plugin* can be configured with the `neonbeeModule` extension.

```groovy
neonbeeModule {
    componentName = 'example'       // will be used for maven publish archivesBaseName
    componentGroup = 'org.example'  // will be used for maven publish group
    componentVersion = '0.0.1'      // will be used for maven publish version
    neonbeeVersion = '0.10.0'       // The NeonBee version to build the module against.
}
```

The plugin can be added with the following code to your `build.gradle` of the root project:

```groovy
plugins {
    id 'io.neonbee.gradle.kickstart.module' version '0.1.0'
}
```

## Quality Plugin

The *Quality Plugin* helps to apply the same quality checks as applied in NeonBee. The plugin will
add several static code checkers with the same configuration as in NeonBee. The configuration files will be
copied to the `{projectDir}/gradle` folder and can be modified if required.

| Code Checker | Config Path         |
|--------------|---------------------|
| CheckStyle   | `gradle/checkstyle` |
| PMD          | `gradle/pmd`        |
| SpotBugs     | `gradle/findbugs`   |
| Spotless     | `gradle/spotless`   |
| ErrorProne   | -                   |
| Jacoco       | -                   |

### Quality Configuration

The *Quality Plugin* can be configured with the `neonbeeQuality` extension.

```groovy
neonbeeQuality {
    minCodeCoverage = 80.0 // The minimum required code coverage (Default: 80.0)
    severityLevel = 'ERROR' // The severity level (INFO, WARN, ERROR) for the *Violations Plugin* (Default: WARN)
}
```

The plugin can be added with the following code to your `build.gradle` of the root project:

```groovy
plugins {
    id 'io.neonbee.gradle.kickstart.quality' version '0.1.0'
}
```

⚠️The *Quality Plugin* can only be applied to gradle projects which applied the *Module Plugin*,
*Application Plugin* or at least  the *Gradle Java Plugin*.

## Models Plugin

The *Models Plugin* helps to compile *cds* files to *edmx* and *csn* and is required
if you want to use `EntityVerticles`.

In case you need `Models` in your *NeonBee Module* or Application run the following command:

```bash
./gradlew initModels
```

⚠️The `ìnitModels` task is only available if your root project has applied the *Application Plugin*
or *Module Plugin*.

This will add a *models* subproject to your NeonBee module with the following content:

- build.gradle
- package.json
- Example.cds
- .gitignore

It will also add the required models dependency to your `build.gradle` file.

## Plugin Development

### Formatter

There is a spotless groovy formatter with some default settings configured.

### Tests

Until now no tests for the plugin exists, except manual tests.

To run and test the plugin, you need to ...

1. ... publish your modified plugin version to maven local via `./gradlew publishToMavenLocal`.
2. ... create a new empty Gradle project.
3. ... add your plugin version to the new gradle project.

Don't forget to add `mavenLocal()` to the `repositories` of the `pluginManagement`.

The `settings.gradle` should look like this:

```groovy
pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}
```

## Support, Feedback, Contributing

This project is open to feature requests/suggestions, bug reports etc. via [GitHub issues](https://github.com/SAP/neonbee-plugin-kickstart/issues). Contribution and feedback are encouraged and always welcome. For more information about how to contribute, the project structure, as well as additional contribution information, see our [Contribution Guidelines](CONTRIBUTING.md).
