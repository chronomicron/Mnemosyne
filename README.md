# Mnemosyne

Mnemosyne is a project to build practical, accessible tools that support older adults and the people who care for them. The current product direction is a connected suite of three apps, developed in small steps:

- **MindSteps from Mnemosyne** — a tablet app for seniors, with approachable interactive games and a private history of activity and performance.
- **KinBridge from Mnemosyne** — a phone app for a primary caregiver to stay connected and, with the senior's authorization, review shared information.
- **Mnemosyne desktop** — a desktop app for social workers, nurses, or care teams to manage and review a caseload, potentially around a dozen seniors.

The apps have different audiences and should each be useful for their main user. They may share selected data through clear, versioned interfaces, with access limited to the people authorized to see it. Local and offline use is preferred where it fits; cloud services and automatic synchronization can be added when a concrete workflow needs them.

## Current state

The active implementation is an early **MindSteps Android prototype**. It has two local games—picture-pair matching and picture-pattern continuation—and records basic game-session and task counts in an on-device SQLite database. The history is not yet displayed in the app and is not synchronized. The initial target for installation practice is a TCL 502 (T432J) running Android 13, with emulator use during development.

KinBridge and Mnemosyne desktop are product plans, not implemented apps yet. Product and technical ideas are maintained in [brainstorming ideas](brainstorming%20ideas.md), the [Development Playbook](Development%20playbook.md), and the [MindSteps Exercise Library](exercises.md).

## Build and install the Android prototype

The Android project is in [android/](android/). Follow [Android setup and install instructions](android/ANDROID.md) to build a local debug APK and install it on an emulator or a USB-connected phone. The debug APK is for development and local installation.

## Project working guide

[AGENTS.md](AGENTS.md) contains practical instructions for coding work in this repository, including audience, privacy, security, scalability, and source-file headers. The [Development Playbook](Development%20playbook.md) describes the broader product and engineering approach.
