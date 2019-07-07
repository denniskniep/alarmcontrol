# Release

## Create a release branch
`git checkout develop`

`git pull`

`git checkout -b release/vX.X.X`

## Modifications
* Adjust Version in [../pom.xml](../pom.xml)
* Write the [../CHANGELOG.md](../CHANGELOG.md) for the new version. Actually it should be simply copied from the unreleased section to the new version.

