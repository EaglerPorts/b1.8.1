plugins {
	base
}

allprojects {
	apply(plugin = "eclipse")

	repositories {
		mavenCentral()
	}

  pluginManager.withPlugin("java") {
    extensions.configure<JavaPluginExtension> {
      toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
      }
    }
  }
}