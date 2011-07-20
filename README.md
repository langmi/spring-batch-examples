# Spring Batch Examples

Collection of Spring Batch Examples.

## Why ?

Spring Batch Examples exist, because i needed a central place to store the source code of my own examples collection. Instead of the usual private Subversion repository i wanted to give Github a try.

To further simplify it, all work is under Apache 2.0 license (see [license wiki page][1] for more details), fork it, use it, bend it and if you find a bug or improvement, do not hesitate to push a patch.

## What ?

List of examples, for some i provided detailed wiki pages

* **skip** see [simple skip job][3]
* **skip-policy** same as [simple skip job][3] but i used a skip policy instead of the usual skip-limit and skippable-exceptions
* **rename-files-tasklet** see [rename files][4], contains a simple and a more generic solution

## General Informations

All Spring Batch Examples:

* are individual maven projects, the pom.xml in the root directory is only for a convenient _build all_ feature
* are tested with:
  * Spring Batch 2.1.8.RELEASE
  * Spring Framework 3.0.5.RELEASE
* are provided "as is", no guarantees :-)
* work with in-memory database, but not with in-memory jobrepository, since the [MapJobRepository is threadsafe][2] i could use it, but why break a standard configuration ?

## License

Apache 2.0 see [license wiki page][1] for more details

[1]: https://github.com/langmi/spring-batch-examples/wiki/License---Apache-2.0
[2]: https://jira.springsource.org/browse/BATCH-1541
[3]: https://github.com/langmi/spring-batch-examples/wiki/Simple-Skip-Job
[4]: https://github.com/langmi/spring-batch-examples/wiki/Rename-Files-Tasklet

