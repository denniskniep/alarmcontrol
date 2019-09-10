# Release

## Create a release branch from develop
`git checkout develop`

`git pull`

`git checkout -b release/vX.X.X`

## Modifications
* Adjust Version in [../pom.xml](../pom.xml)
* Adjust Version in [../frontend/package.json](../frontend/package.json)
* Write the [../CHANGELOG.md](../CHANGELOG.md) for the new version. Actually it should be simply copied from the unreleased section to the new version.
* Adjust Version in [../docker-compose.yaml](../docker-compose.yaml)

## E2E Tests
Manually execute End to End Tests with the following script:
```
GRAPHHOPPER_APIKEY="replaceMe" \
MAPBOX_ACCESS_TOKEN="replaceMe" \
e2eTests/executeE2ETests.sh
```

## Commit and Push
After `git commit -m 'Release vX.X.X'` execute a `git push`

## Create PR into develop
On GitHub create a PullRequest from branch `release/vX.X.X` into branch `develop`

Merge the PullRequest

## Create PR into master
On GitHub create a PullRequest from branch `release/vX.X.X` into branch `master`

Merge the PullRequest

Automatically the master branch is build and released by AzureDevOps

## Check Release
* Start [../docker-compose.yaml](../docker-compose.yaml) and ensure application is working


