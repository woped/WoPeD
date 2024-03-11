# WoPeD Client

Website: https://woped.dhbw-karlsruhe.de/ <br>
Responsible: Thomas Freytag, thomas.freytag@dhbw-karlsruhe.de

WoPeD provides easy-to-use software for editing, modelling, simulating and analyzing processes described by workflow nets. Workflow nets are a class of [Petri nets](https://en.wikipedia.org/wiki/Petri_net) especially useful in process modelling, process mining, and workflow systems.

## Configuration for development
In order to develop on your local machine, `mvn install` is required in a first step. 
Since some of our dependencies are available via `GitHub Packages`, the following steps are required before you can run `mvn install`. 
1. Create personal access token [Account -> Developer Settings -> Personal access token]
2. Create file `~/.m2/settings.xml`
3. Copy content of [GitHub Guide](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry) into the xml file of step 2 and replace the username and token with your data.
