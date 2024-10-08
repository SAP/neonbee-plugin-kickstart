package io.neonbee.gradle.kickstart.internal.plugins

import static org.gradle.api.plugins.JavaPlugin.COMPILE_JAVA_TASK_NAME
import static org.gradle.api.plugins.JavaPlugin.COMPILE_TEST_JAVA_TASK_NAME

import java.nio.file.Path

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider

import com.github.spotbugs.snom.Confidence
import com.github.spotbugs.snom.Effort
import com.github.spotbugs.snom.SpotBugsExtension

import io.neonbee.gradle.kickstart.internal.GradleHelper

class SpotBugsPlugin implements Plugin<Project> {
    private static final String SPOT_BUGS_MAIN_TASK_NAME = 'spotbugsMain'
    private static final String SPOT_BUGS_TEST_TASK_NAME = 'spotbugsTest'
    private static final String SPOT_BUGS_CONFIG_DIR = 'gradle/findbugs'

    @Override
    void apply(Project project) {
        project.pluginManager.apply(com.github.spotbugs.snom.SpotBugsPlugin)

        if (!project.file(SPOT_BUGS_CONFIG_DIR).exists()) {
            Path targetDir = project.file('gradle').toPath()
            GradleHelper.copyResourceDir('findbugs', targetDir)
        }

        project.configure(project.extensions.getByType(SpotBugsExtension)) {
            // ignore the issues during SpotBugs run, because the violations plugin
            // will display the issues and let the build fail later.
            ignoreFailures = true
            toolVersion = '4.8.3' // https://github.com/spotbugs/spotbugs/releases
            effort = Effort.valueOf('MAX')
            reportLevel = Confidence.valueOf('MEDIUM')
            excludeFilter = project.file(SPOT_BUGS_CONFIG_DIR + '/excludeFilter.xml')
        }

        TaskProvider<Task> spotbugsMain = project.tasks.named(SPOT_BUGS_MAIN_TASK_NAME) {
            // only one report can be configured at a time.
            reports {
                xml.required.set true
                html.required.set false
            }
        }
        project.tasks.named(COMPILE_JAVA_TASK_NAME) {
            finalizedBy spotbugsMain
        }

        TaskProvider<Task> spotbugsTest = project.tasks.named(SPOT_BUGS_TEST_TASK_NAME) {
            // only one report can be configured at a time.
            reports {
                xml.required.set true
                html.required.set false
            }
        }
        project.tasks.named(COMPILE_TEST_JAVA_TASK_NAME) {
            finalizedBy spotbugsTest
        }
    }
}
