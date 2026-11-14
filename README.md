# Mnemosyne

Mnemosyne is a project to build practical, accessible tools that support older adults and the people who care for them. The current product direction is a connected suite of three apps, developed in small steps:

The suite is primarily designed to help the caregiver, who may arrange and pay for the service and set up the senior's devices. The senior remains the direct user of MindSteps on their phone or tablet; the care professional is the main user of the desktop web app.

- **MindSteps from Mnemosyne** — an Android and future iOS app used directly by the senior, primarily on a carried tablet. Enjoyable games encourage regular use and provide activity statistics for the caregiver, including periodic sets of three or four activities. MindSteps also provides simple caregiver-configured reminders, resident-side GPS/location support, route prompts, caregiver check-ins, voice messages, and intercom. Its senior-facing interface is designed to stay predictable, avoid interrupting games, and save progress for later resumption. KinBridge shows shared activity/location information and can alert the caregiver if the senior leaves home or a familiar route corridor.
- **KinBridge from Mnemosyne** — an Android and iOS phone app for a primary caregiver to stay connected and, with the senior's authorization, review shared information.
- **Mnemosyne desktop** — a browser-based web app for social workers, nurses, or care teams to manage and review a caseload, potentially around a dozen seniors, and share resident-specific updates with authorized caregivers through KinBridge.

The apps have different audiences and should each be useful for their main user. Android and Apple clients will be separate installable apps; the code-sharing approach is still to be decided. The desktop experience is intended to run in a web browser across common computer platforms. The clients may share selected data through clear, versioned interfaces, with access limited to the people authorized to see it. Local and offline use is preferred where it fits; cloud services and automatic synchronization can be added when a concrete workflow needs them.

## Current state

The active implementation is an early **MindSteps Android prototype**. It has two local games—picture-pair matching and picture-pattern continuation—and records basic game-session and task counts in an on-device SQLite database. The history is not yet displayed in the app and is not synchronized. Development and testing start on Android phones and tablets, including the TCL 502 (T432J) running Android 13 and an emulator. The technology choices should leave room for a future iPhone app.

KinBridge and Mnemosyne desktop are product plans, not implemented apps yet. Product and technical ideas are maintained in [brainstorming ideas](brainstorming%20ideas.md), the [Development Playbook](Development%20playbook.md), and the [MindSteps Exercise Library](exercises.md).

Shared MindSteps–KinBridge location and communication concepts captured from the **Mnemosyne Vocal** chat are collected in [KinBridge/location-and-voice.md](KinBridge/location-and-voice.md).

## Build and install the Android prototype

The Android project is in [android/](android/). Follow [Android setup and install instructions](android/ANDROID.md) to build a local debug APK and install it on an emulator or a USB-connected phone. The debug APK is for development and local installation.

## Project working guide

[AGENTS.md](AGENTS.md) contains practical instructions for coding work in this repository, including audience, privacy, security, scalability, and source-file headers. The [Development Playbook](Development%20playbook.md) describes the broader product and engineering approach. The [MindSteps design requirements](MindSteps/requirements.md) is the working brief for tablet layout, accessibility, assets, and setup decisions.
