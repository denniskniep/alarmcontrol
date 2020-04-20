# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
### Changed
### Fixed

## [1.3.0] - 2020-04-20
### Changed
- ReferenceIds must not anymore be unique for an organisation. There is a timeframe where the referenceId treated as active. Same with AlertCalls.
- Refine parsing of Pager Alert Text
- Changed Restart Policy for Container to always

## [1.2.3] - 2020-01-31
### Fixed
- Fix AlertView not displayed if Alert from version before 1.2

## [1.2.2] - 2020-01-30
### Fixed
- location column to not null

## [1.2.1] - 2020-01-30
### Fixed
- Docker-Compose Version
- Liquibase db changelog

## [1.2.0] - 2020-01-30
### Added
- View and delete Alerts in ManageUi 
- Write logfile as JSON
- Log all HttpRequests (incoming/outgoing) with Logbook and MDC
- Option to start preconfigured Logback
- Send Notifications on Alert Received
- Send Notifications via E-Mail
- Configure and display AAO ("Alarm- und Ausrückordnung")
- Send Notifications via Push Message (Firebase)
- Severity for Notifications
- Receive Notifications via Push Message (Firebase) in PagerPWA (Progressive WebApp)
- Display Link to PagerPWA with Configuration for Firebase Push Messages
- Form to generate a testalarm
- Scrape Actuator Metrics with Prometheus and display in Grafana

### Changed
- Graylog update from 3.0 to 3.1.2
- Filebeat and elasticsearch update from 6.6.1 to 6.8.4
- Replace Graylogstack with Filebeat, ElasticSearch & Kibana
- Restructured repository folders 
- Use OKHttpClient

### Fixed
- Gzipped Body of HTTP Messages are now logged uncompressed
- Refresh menu with alerts on alert added notification
- Two parallel incoming Alert Calls for single Alert ReferenceId

## [1.1.1] - 2019-08-09
### Fixed
- Optimize common employee status view for more employees
- Fix menu entry if no keyword and address is present

## [1.1.0] - 2019-08-03
### Added
- Separate `docker-compose.yaml` file with image from [DockerHub Repository](https://hub.docker.com/r/denniskniep/alarmcontrol)
- Refresh Menu "Last Alerts" if Alarm received
- Display and manage alert independent employee status

### Changed
- Display raw address if there could be no address geocoded

### Fixed
- Minutes in Dates are displayed correctly 
- Display date after 24 Hours in Alarmcounter
- Make house number in Mapboxgeocoding result optional
- Do not use geocoding result if it is too inaccurate
- Fix conversion of double value that has no floating point

## [1.0.1](https://github.com/denniskniep/alarmcontrol/compare/v1.0.0-24...v1.0.1-30) - 2019-07-07
### Added
- CI/CD with AzureDevOps
- Introduce changelogs

## [1.0.0] - 2019-07-06
Initial Release
