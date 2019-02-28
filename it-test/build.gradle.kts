/*
 * Copyright (C) 2019 Knot.x Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.nosphere.apache.rat.RatTask

plugins {
    id("java-library")
    id("org.nosphere.apache.rat") version "0.4.0"
}

// -----------------------------------------------------------------------------
// Dependencies
// -----------------------------------------------------------------------------
dependencies {
    implementation(project(":knotx-server-http-core"))
    implementation(project(":knotx-assembler"))
    implementation(project(":knotx-splitter-html"))

    testImplementation(platform("io.knotx:knotx-dependencies:${project.version}"))
    testImplementation("io.knotx:knotx-junit5")
    testImplementation("io.knotx:knotx-launcher")
    testImplementation(group = "io.knotx", name = "knotx-launcher", classifier = "tests")

    testImplementation(group = "io.vertx", name = "vertx-junit5")
    testImplementation(group = "io.vertx", name = "vertx-unit")
    testImplementation(group = "io.rest-assured", name = "rest-assured", version = "3.3.0")
}

// -----------------------------------------------------------------------------
// Tasks
// -----------------------------------------------------------------------------
tasks {
    named<RatTask>("rat") {
        excludes.addAll("**/build/*", "**/out/*")
    }
    getByName("build").dependsOn("rat")
}

sourceSets.named("test") {
    resources.srcDir("../conf")
}