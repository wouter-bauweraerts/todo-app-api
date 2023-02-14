# TODO App Workshop

Example app that displays how to automate build, testing and deploys with github

## How-to get the party started

The accompanying Virtual machine should be copied to your local hard drive.

### Development Server

* Download and install [virtualbox](https://www.virtualbox.org/)
* Import the appliance on virtual box
* Make sure the network is set on bridged

Username: ubuntu
Password: password

The development server is installed with all te necessary tools to start this workshop.

### Docker configuration

To be able to run docker as a user you will need to add your user to the docker group

```shell
sudo usermod -aG docker ubuntu
newgrp docker
```

### Kubernetes configuration

Configure helm and k9s to deploy to the correct cluster

```shell
microk8s config > ~/.kube/config
```

### Self-hosted runner

Configure a new runner in your github project

* Goto settings (tab on top)
* In the left menu: actions->runners
* Green button at the top new self-hosted runner
* execute the configure step uitvoeren in /home/ubuntu/actions-runner (without run.sh)
* Install service

```shell
ubuntu@ubuntu:~/actions-runner$ sudo ./svc.sh install
```

* Execute runner

```shell
ubuntu@ubuntu:~/actions-runner$ sudo ./svc.sh start
```
