# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]
### Added
- View and delete Alerts in ManageUi 
- Write logfile as JSON
- Option to start preconfigured Logback

### Changed
### Fixed

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