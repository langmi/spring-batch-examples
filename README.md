# Spring Batch Examples

Collection of Spring Batch Examples, covering the basics, [readers][readers], [writers][writers], [listeners][listeners] and complex usecases.

## Why?

Spring Batch Examples exist, because i needed a central place to store the source code of my own examples collection. Instead of the usual private Subversion repository i wanted to give Github a try.

## What?

The Examples are described in each of the submodule README.

## How?

This repository uses [git submodules][git-submodules] to link to the individual example repositories on github. To get the latest commits for the submodules:

* [update submodules from with git command][git-update-submodules]: `git submodule -q foreach git pull -q origin master`
* [sourcetree][sourcetree]: just fetch/pull from the current repository, sourcetree will update the submodules too

## General Informations

All Spring Batch Examples:

* are individual github repositories and maven projects, the pom.xml in this root directory is only for a convenient _build all_ feature
* are tested with:
  * Spring Batch 2.1.8.RELEASE
  * Spring Framework 3.1.0.RELEASE
* are provided "as is", no guarantees :-)
* work with [in-memory database][database], but not with in-memory jobrepository, since the [MapJobRepository is threadsafe][maprepo-threadsafe] i could use it, but why break a standard configuration ?

Overview for the project setup.

### Maven Configuration

The pom.xml in this root directory contains just a simple parent pom wrapper project to provide a convenient "build all" feature, see [Maven Pom project aggregation][project-aggregation].
Each individual project stands on its own and can be used as such, e.g. there are no maven configurations made in the parent pom.

The examples modules are:

* spring batch examples parent, the mentioned "build all" parent module
    * complex, contains examples which use more than one of the core aspects of spring batch
    * listeners
    * playground, mostly for incubating new examples
    * readers
    * writers

#### Usable Systemproperties

* -DreuseForks
    * run tests with individual and fresh JVM per test class or reuse JVM between tests
    * default is `false`
    * use `true`, if you want to run the tests with shared JVMs

#### Specific Build Configurations

For each project i added specific build configurations for the following build plugins:

* [maven-compiler-plugin][maven-compiler-plugin]
    * JDK set to 1.6
    * compiler.debug=true
    * compiler.optimize=false
* [maven-enforcer-plugin][maven-enforcer-plugin]
    * enforce minimum Java version 1.6
    * enforce minimum Maven version 3.0
* [maven-resources-plugin][maven-resources-plugin]
    * forced UTF-8 encoding
* [maven-surefire-plugin][maven-surefire-plugin]
    * set log4j properties file location
    * Java memory configuration to prevent OutOfMemory problems during tests, see [Java -X options][java-x-options]
    * [forkCount][forkCount]=1C to run one test class per cpu core
    * [reuseForks][reuseForks]=false to run each test class isolated
        * can be overridden by using systemproperty `-DreuseForks=true`

##### Why isolated tests?

I use the [reuseForks=false][reuseForks] for the maven test configuration to get a new JVM for each test class. This configuration lowers the test run time by a signifikant amount. Why is that necessary?

While using the HSQLDB in-memory database i see sometimes thread problems.

#### Dependencies

Each project contains only the needed dependencies and i check regularly for version updates and the [dependencies licenses][license].

### Directory Structure

The project follows the [maven standard directory layout][standard-dir-layout], only difference so far is a README.md (md for markdown format) and a LICENSE file instead of both files ending with .txt.

### Important /resources Directories and Files

Overview:

    # log4j configuration
    src/main/resources/log4j/log4.properties
    # the job configurations    
    src/main/resources/spring/batch/job/
    # spring batch setup
    src/main/resources/spring/batch/setup/job-context.xml
    src/main/resources/spring/batch/setup/job-database.xml
    # the used input files    
    src/test/resources/input/ 
    # test setup
    src/test/resources/spring/batch/setup/test/job-test-context.xml

For each project:

* the log4j.properties is under `src/main/resources/log4j/log4j.properties`
    * logging level is WARN for all and DEBUG for the source package of the project
    * location might be changed soon to src/test...
* the Spring Batch infrastructure setup is under `src/main/resources/spring/batch/setup/...`
    * `job-context.xml` contains JobRepository, StepScope etc. beans
    * `job-database.xml` contains the datasource and transactionmanager beans
        * [HSQLDB in-memory][hsqldb-in-memory] variant is used
        * Spring Batch Schema is loaded at Application Context startup with [jdbc:initialize-database][jdbc-init-db]
* the Spring Batch test infrastructure setup is under `src/test/resources/spring/batch/setup/test/...`
    * `job-test-context.xml` contains just the [JobLauncherTestUtils][JobLauncherTestUtils] bean


### Code Structure

* each example has its own package (test package has the same name), e.g. `simplelist`
    * not all examples have java source, some have only a job.xml and some tests
* each example has its own job.xml, e.g. `simple-list-job.xml`
* each example has a large test coverage, well what can i say, i am addicted to tests :-)

## License

To simplify it, all work is under [Apache 2.0 license][apache-license], fork it, use it, bend it and if you find a bug or improvement, do not hesitate to push a patch.

### Licenses of used Java libraries

**last check: 29.04.2013**

* AOP Alliance - [Public Domain][aop-dependency]
* Apache commons-collections, -dbcp, -io, -logging, -pool - all licensed under Apache 2.0
* bcprov - [MIT][bcprov-dependency]
* harmcrest-core - [new BSD][hamcrest-dependency]
* HSQLDB - [based on BSD][hsqldb-dependency]
* Jettison - [Apache 2.0 license][jettison-dependency]
* log4j - [Apache 2.0 license][log4j-dependency]
* Maven - [Apache 2.0 license][maven-dependency]
* slf4j - [identical to MIT License][slf4j-dependency]
* Spring Batch - [Apache 2.0 license][spring-batch-dependency]
* Spring Framework - all licensed under [Apache 2.0][spring-framework-dependency]
* xpp3 - XMLPullParser - [Public Domain][xpp3-dependency]
* xstream - [BSD][xstream-dependency]

#### Problematic Dependencies

* JUnit - [Common Public License - v 1.0][junit-dependency] (look for license.txt in the github repository)
    * just do not distribute your project with junit, should be easy
* Truezip - [Eclipse Public License, Version 1.0][truezip-dependency]
    * if you distribute a project with truezip you need to include the license and a statement basically saying you did not change anything
    * if you change the source, ... well if you run a commercial product you are screwed :-)


[hsqldb-in-memory]: http://hsqldb.org/doc/2.0/guide/running-chapt.html#running_inprocess-sect
[jdbc-init-db]: http://static.springsource.org/spring/docs/current/spring-framework-reference/html/jdbc.html#d0e24263
[JobLauncherTestUtils]: http://static.springsource.org/spring-batch/apidocs/org/springframework/batch/test/JobLauncherTestUtils.html
[maven-enforcer-plugin]: http://maven.apache.org/enforcer/maven-enforcer-plugin/
[maven-compiler-plugin]:http://maven.apache.org/plugins/maven-compiler-plugin/
[maven-resources-plugin]:http://maven.apache.org/plugins/maven-resources-plugin/
[standard-dir-layout]: http://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html
[maven-surefire-plugin]:http://maven.apache.org/plugins/maven-surefire-plugin/
[project-aggregation]:http://maven.apache.org/guides/introduction/introduction-to-the-pom.html#Project_Aggregation

[aop-dependency]: http://aopalliance.sourceforge.net/
[bcprov-dependency]: http://www.bouncycastle.org/licence.html
[hamcrest-dependency]: https://github.com/hamcrest/JavaHamcrest
[hsqldb-dependency]: http://hsqldb.org/web/hsqlLicense.html
[jettison-dependency]: http://jettison.codehaus.org/License
[junit-dependency]: https://github.com/junit-team/junit
[log4j-dependency]: http://logging.apache.org/log4j/1.2/license.html
[maven-dependency]: http://maven.apache.org/license.html
[slf4j-dependency]: http://www.slf4j.org/license.html
[spring-batch-dependency]: http://static.springsource.org/spring-batch/license.html
[spring-framework-dependency]: http://www.springsource.org/about
[truezip-dependency]: http://truezip.java.net/license.html
[xpp3-dependency]: http://www.xmlpull.org/
[xstream-dependency]: http://xstream.codehaus.org/license.html


[git-update-submodules]: http://stackoverflow.com/a/9103113/62201
[apache-license]: http://www.apache.org/licenses/LICENSE-2.0.html
[database]:http://hsqldb.org/doc/2.0/guide/running-chapt.html#running_inprocess-sect
[git-submodules]: http://git-scm.com/book/de/Git-Tools-Submodules
[java-x-options]: http://docs.oracle.com/cd/E13150_01/jrockit_jvm/jrockit/jrdocs/refman/optionX.html
[listeners]: http://static.springsource.org/spring-batch/reference/html/configureStep.html#interceptingStepExecution
[maprepo-threadsafe]: https://jira.springsource.org/browse/BATCH-1541
[readers]: http://static.springsource.org/spring-batch/reference/html/domain.html#domainItemReader
[reuseForks]: http://maven.apache.org/surefire/maven-surefire-plugin/test-mojo.html#reuseForks
[forkCount]: http://maven.apache.org/surefire/maven-surefire-plugin/test-mojo.html#forkCount
[sourcetree]: http://sourcetreeapp.com/
[writers]: http://static.springsource.org/spring-batch/reference/html/domain.html#domainItemWriter