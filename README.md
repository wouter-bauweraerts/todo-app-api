# TODO App Workshop

Example app that displays how to automate build, testing and deploys with github

## How-to get the party started

- The accompanying Virtual machine should be copied to your local hard drive.
- Fork this repo to your local Github account
- Fork [todo-front repo](https://github.com/wouter-bauweraerts/todo-front)

### Development Server

- Download and install [virtualbox](https://www.virtualbox.org/)
- Import the appliance on virtual box
- Make sure the network is set on bridged. [Networksettings explained](https://www.virtualbox.org/manual/ch06.html#network_bridged)

```
Username: ubuntu
Password: password
```

The development server is installed with all te necessary tools to start this workshop.

### Kubernetes configuration

Configure helm and k9s to deploy to the correct cluster

```shell
microk8s config > ~/.kube/config
```

### Self-hosted runner

#### API Repo

Configure a new runner in your API github project

- Goto settings (tab on top)
- In the left menu: actions->runners
- Green button at the top new self-hosted runner
- execute the configure step in /home/ubuntu/actions-runner-api (without run.sh)
- Install service

```shell
ubuntu@ubuntu:~/actions-runner-api$ sudo ./svc.sh install
```

- Execute runner

```shell
ubuntu@ubuntu:~/actions-runner-api$ sudo ./svc.sh start
```

#### Front Repo

Do the same steps for your todo-front repo

- Goto settings (tab on top)
- In the left menu: actions->runners
- Green button at the top new self-hosted runner
- execute the configure step in /home/ubuntu/actions-runner-front (without run.sh)
- Install service

```shell
ubuntu@ubuntu:~/actions-runner-front$ sudo ./svc.sh install
```

- Execute runner

```shell
ubuntu@ubuntu:~/actions-runner-front$ sudo ./svc.sh start
```
