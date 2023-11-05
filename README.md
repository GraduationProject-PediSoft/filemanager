# FileManager Pedisoft

This repository contains the file manager for Pedisoft sytem. It uses Springboot  to connect it with Minio Object Storage. Saves files, downloads files and generates unique ids for them. It also runs and scheduled service to delete info in the bucket.

# Running

The project was designed to be executed in a containerized environment. In this repo [Docker](https://www.docker.com/) was used for it, but Podman or Kubernetes could be used with little configuration. The compose file builds and executes the SpringBoot web server without any additional configuration, simply run:

```sh
docker compose up -d
```
The compose also runs a [Minio](https://min.io/) container that the microserver connects to using the Minio dependency 
# Configuration
## Environment
the *.env_template* file contains the environment variables that should be defined in order to run, should be edited according to the needs.
