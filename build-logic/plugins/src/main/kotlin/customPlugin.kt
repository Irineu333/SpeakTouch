/*
 * Custom plugin.
 *
 * Copyright (C) 2023-2025 Patryk Mis.
 * Copyright (C) 2023 Irineu A. Silva.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class CustomPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("versionConfig", VersionConfig::class.java)

        project.plugins.withId("com.android.application") {
            val androidComponents = project.extensions.getByType(ApplicationAndroidComponentsExtension::class.java)

            androidComponents.onVariants { variant ->
                variant.outputs.forEach { output ->
                    output.versionCode.set(extension.getCode())
                    output.versionName.set(extension.getName())
                }
            }

            project.tasks.register("appVersion") {
                doLast {
                    println(
                        "versionCode is ${extension.getCode()} " +
                                "from versionName ${extension.getName()}"
                    )
                }
            }
        }
    }

    private fun Project.android(): ApplicationExtension {
        return extensions.getByName("android") as ApplicationExtension
    }
}
