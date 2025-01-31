/*
 * App version representation.
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

open class VersionConfig {

    var major: Int = -1 // 0..Infinity
    var minor: Int = -1 // 0..9
    var patch: Int = -1 // 0..9
    var type: Type? = null

    fun getCode(): Int {
        checkIfIsValid()
        val majorValue = major * MAJOR_WEIGHT
        val minorValue = minor * MINOR_WEIGHT
        val patchValue = patch * PATCH_WEIGHT
        return majorValue + minorValue + patchValue
    }

    fun getName(): String {
        checkIfIsValid()
        val versionName = "$major.$minor.$patch"
        return when (type!!) {
            Type.RELEASE -> versionName
            Type.DEV -> "$versionName-dev"
        }
    }

    private fun checkIfIsValid() {
        require(major in 0..Int.MAX_VALUE) { "Invalid major version" }
        require(minor in 0..9) { "Invalid minor version" }
        require(patch in 0..9) { "Invalid patch version" }
        require(type != null) { "Version type not defined" }
    }

    enum class Type {
        DEV,
        RELEASE
    }

    companion object {
        private const val MAJOR_WEIGHT = 100
        private const val MINOR_WEIGHT = 10
        private const val PATCH_WEIGHT = 1
    }
}
