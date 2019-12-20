# csw-client

This project contains an interactive shell and allows its users to gain access to all the major csw services via CLI 
which then can be used to communicate with a HCD (Hardware Control Daemon) and an Assembly using 
TMT Common Software ([CSW](https://github.com/tmtsoftware/csw)) APIs and with a Sequencer using 
TMT Executive Software ([ESW](https://github.com/tmtsoftware/esw)). 

## Build Instructions

The build is based on sbt and depends on libraries generated from the 
[csw](https://github.com/tmtsoftware/csw) and [esw](https://github.com/tmtsoftware/esw) project.

## Prerequisites for running Components

The CSW services need to be running before starting the components. 
This is done by starting the `csw-services.sh` script, which is installed as part of the csw build.
If you are not building csw from the sources, you can get the script as follows:

 - Download csw-apps zip from https://github.com/tmtsoftware/csw/releases.
 - Unzip the downloaded zip.
 - Go to the bin directory where you will find `csw-services.sh` script.
 - Run `./csw_services.sh --help` to get more information.
 - Run `./csw_services.sh start` to start the location service and config server.

## Running the csw-client

After making sure that all the pre-requisites are satisfied, we can directly run the client via sbt 
from the root directory of the project

 - Run `sbt run` 

We can also run it via binary file generated after staging the project 
 - Run `sbt universal:stage`
 - Navigate to `/target/universal/stage/bin` 
 - Run `./csw-client`

## Usage of Command Service to interact with HCDs, Assemblies and Sequencers 

### Finding the required component

Get handle to the command service for a particular HCD/Assembly/Sequencer using following commands within csw-client repl
 - For HCDs
 `val hcdComponent = hcdCommandService("SampleHcdName")`
 - For Assemblies
 `val assemblyComponent = assemblyCommandService("SampleAssemblyName")`
 - For Sequencers
 `val sequencer = sequencerCommandService(Subsystem, "darknight")`
 
**SampleHcdName** and **SampleAssemblyName** are the names by which both HCD and Assembly components were registered 
with location service respectively.

**Subsystem** and **darknight** are the subsystem and the observing mode for the sequencer

Note - The above calls internally uses location service to resolve the required HCD/Assembly/Sequencer.

### Creating the commands to submit to HCD/Assembly

Create a setup command object using similar command to what is shown below

`val setup = Setup(Prefix("sample.prefix"),CommandName("setup-command"),Some(ObsId("sample-obsId")))`

### Creating the sequence to submit to Sequencer

`val setup = Setup(Prefix("sample.prefix"),CommandName("setup-command"),Some(ObsId("sample-obsId")))`

`val sequence = Sequence(setup)`

### Submitting the commands to components

Submit the setup command object created in previous step using command service for the HCD/Assembly
 - `val hcdResponse = hcdComponent.submit(setup).get` 
 - `val assemblyResponse = assemblyComponent.submit(setup).get`
 
Submit the sequence object created in previous step using command service for the Sequencer
 - `val sequencerResponse = sequencer.submit(sequence).get`