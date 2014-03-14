## KPCC Reader for Android

## Releasing
For each release (anything that gets pushed to the Play store in any form):
* Bump the versionName and versionCode appropriately in AndroidManifest.xml.
* Run the `build-release` script.
* Upload the new APK to the Play store in the appropriate section (Alpha, Beta, Production).
* Make an announcement in the appropriate Google Group to let testers know about the update.

## Version Name
In general we'll follow Semantic Versioning. For X.Y.Z:
* X gets updated for major "breaking" changes, such as major UI/UX changes or dropped support for
 older devices.
* Y gets updated when additional features are added.
* Z gets updated for bug fixes.

During the initial rapid development (pre-beta) phase, none of this matters and we'll only loosely
following semver.
