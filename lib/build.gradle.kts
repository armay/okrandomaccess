group = "com.github.armay"
version = "0.1.0"

plugins {
    val dokkaVersion = "1.4.32"
    val kotlinVersion = "1.5.0"
    kotlin("jvm") version kotlinVersion
    id("org.jetbrains.dokka") version dokkaVersion
    `java-library`
    `maven-publish`
    signing
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "okrandomaccess"
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name.set("OkRandomAccess")
                description.set("OkRandomAccess is a small extension for Okio introducing random access functionality to sinks and sources on the JVM.")
                url.set("https://github.com/armay/okrandomaccess")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/armay/okrandomaccess/blob/main/LICENSE.md")
                    }
                }
                developers {
                    developer {
                        id.set("armay")
                        name.set("Artur Vaganian")
                        email.set("armay@yandex.ru")
                    }
                }
                scm {
                    val githubUrl = "https://github.com/armay/okrandomaccess"
                    val connectionUrl = "scm:git:$githubUrl.git"
                    connection.set(connectionUrl)
                    developerConnection.set(connectionUrl)
                    url.set(githubUrl)
                }
            }
        }
    }
    repositories {
        maven {
            val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            name = "OSSRH"
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            credentials {
                username = System.getenv("OSSRH_USERNAME")
                password = System.getenv("OSSRH_PASSWORD")
            }
        }
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/armay/okrandomaccess")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}

tasks {
    val javadocJar = named<Jar>("javadocJar")
    javadocJar {
        dependsOn(dokkaHtml)
        from(dokkaHtml)
    }
    compileJava {
        options.release.set(8)
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

dependencies {
    val jimfsVersion = "1.1"
    val okioVersion = "2.10.0"
    api("com.squareup.okio:okio:$okioVersion")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("com.google.jimfs:jimfs:$jimfsVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}
