/*
 * Certain portions of this software are Copyright (C) 2006-present
 * Vanderbilt University, Institute for Software Integrated Systems.
 *
 * Certain portions of this software are contributed as a public service by
 * The National Institute of Standards and Technology (NIST) and are not
 * subject to U.S. Copyright.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above Vanderbilt University copyright notice, NIST contribution
 * notice and this permission and disclaimer notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE. THE AUTHORS OR COPYRIGHT HOLDERS SHALL NOT HAVE
 * ANY OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS,
 * OR MODIFICATIONS.
 */

plugins {
    java
    `maven-publish`
}

val rtiHome = System.getenv("RTI_HOME")

val archivaUser: String by project
val archivaPassword: String by project
val version: String by project
val archivaHostId: String by project
val archivaPort: String by project


dependencies {
    implementation(group="org.apache.logging.log4j", name="log4j-core", version="2.17.1")

    implementation(files("$rtiHome/lib/portico.jar"))

    implementation(group="com.fasterxml.jackson", name="jackson-bom", version="2.13.4.20221013")
    implementation(group="com.fasterxml.jackson.core", name="jackson-databind", version="2.13.4.1")
    implementation(group="edu.vanderbilt.vuisis.cpswt", name="utils", version=version)


    testImplementation(group="junit", name="junit", version="4.13.2")
    testImplementation(group="org.mockito", name="mockito-core", version="4.1.0")
}

tasks.test {
    setForkEvery(1)
}

publishing {
    publications {
        create<MavenPublication>("root") {
            groupId = "edu.vanderbilt.vuisis.cpswt"
            artifactId = "root"
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "rootPublish"
            val internalRepoUrl = "http://$archivaHostId:$archivaPort/repository/internal"
            val snapshotsRepoUrl = "http://$archivaHostId:$archivaPort/repository/snapshots"
            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else internalRepoUrl)

            logger.info("URL = \"$url\"")
            isAllowInsecureProtocol = true
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                username = archivaUser
                password = archivaPassword
            }
        }
    }
}
