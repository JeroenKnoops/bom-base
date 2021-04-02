# BOM-base

**Description**: BOM-base is a caching repository for bill-of-materials metadata.

(See the [architecture document](docs/architecture.md) for a detailed technical
description.)

This is an _experimental_ tool for evaluating various aspects of the Software
Bill-of-Materials (SBOM) for projects.

While producing a bill-of-materials, a local metadata cache is desirable to 
augment the publicly available metadata with local curations and store 
metadata for packages that are not publicly released.

The service is internally set up as a metadata store with "harvesters" to
collect missing metadata. Harvesters are triggered by the availability
(and modification) of their input information. 

This harvesting mechanism starts from a client tool requesting metadata for a
specific package. If the package is not yet known, it is created in the metadata
store. This change allows one or more harvesters to start collecting metadata
from external sources. One harvester might pull various fields
from [ClearlyDefined](https://clearlydefined.io). The new availability of the
source code location and no scanned license could trigger the license scanning
harvester to download and scan the source code for licenses and other copyright
information. If the scanned license does not match the license declared in the
originating repository, it can be contested by a harvester that checks
consistency between the "declared" and "detected" license fields. When a client
later requests the same package, the currently aggregated metadata is returned
from the service.

A user interface will be provided to allow human curation of contested
information and resolution of incorrect or missing metadata. A manual change of
such metadata can in turn trigger other processes to complete additional fields.

## Dependencies

The service requires Java 11.

## Installation

The software is built by the Maven `mvn clean install` command.

The server is started as a standard Java executable using `java -jar <application-name>.jar`.

## Configuration

(No configuration supported.)

## Usage

After starting up, the service exposes on port 8080:

* An API to provide access to the stored metadata per package.

### Docker

After building the project, you can also run the application with Docker.

Build docker image:
```bash
docker build -f docker/Dockerfile -t bom-base .
```

Run application:
```
docker run -p 8080:8080 bom-base
```

## How to test the software

Unit tests are executed by the Maven `mvn clean test` command.

## Known issues
The software is not suited for production use.

These are the most important topics that are to be addressed:
(A marked checkbox means the topic is in progress.)

- [x] Add manual curation user interface
- [ ] Retry failed harvesting attempts (e.g. when source offline)
- [ ] Handle false information (e.g. non-existing source location)
- [ ] Harvesting metadata from inner source repositories
- [ ] Add harvester for Maven
- [ ] Add harvester for PyPi 
- [ ] Add harvester for NPM
- [ ] Add harvester for APK
- [ ] Add harvester for Debian 
- [ ] Add harvester for NuGet
- [ ] Add harvester for Cargo
- [ ] Persist metadata in database

## Disclaimer
BOM-base is an _experimental_ tool, and not suited for production.

## Contact / Getting help

Submit an issue in the issue tracker of this project.

## License

See [LICENSE.md](LICENSE.md).

## Credits and references

(Empty)


