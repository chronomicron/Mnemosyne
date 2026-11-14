# Mnemosyne Coding Guide

These instructions apply to coding and documentation work throughout the repository unless a more specific `AGENTS.md` in a subfolder adds guidance. Keep the product owner's current instructions and the existing project's decisions in view; this guide is a working reference, not a reason to block ordinary implementation choices.

## Product and users

Mnemosyne is a three-part suite primarily intended to help caregivers. A caregiver may purchase the service, organize setup, and manage sharing; the senior is the direct user of MindSteps activities on the tablet, and a social worker or nurse may use the desktop web app. Keep the buyer/setup role distinct from the daily user role. Keep each product's primary user and device in mind when changing code:

- **MindSteps:** the senior using an Android phone or tablet (primarily a carried tablet, and potentially future iPhone/iPad versions) for welcoming, understandable games and activities, resident-side GPS/location support, route prompts, voice messages, and intercom. Instructions, controls, feedback, and accessibility need to work for someone who may not be comfortable with technology.
- **KinBridge:** the primary caregiver using an Android phone or iPhone to set up the resident profile and familiar places/routes, review shared game statistics and location information, receive alerts when the senior leaves home or a familiar corridor, and communicate with the senior through MindSteps.
- **Mnemosyne desktop:** a social worker, nurse, or care professional using a browser-based web app on a computer to manage a caseload, review separate senior profiles efficiently, and send resident-specific updates to authorized caregivers through KinBridge.

The current codebase is an early Android MindSteps prototype. KinBridge and the desktop web app are planned, not implemented. Work on the prototype should stay focused on usable slices and should not build infrastructure for unimplemented products without a concrete need. The product direction calls for Android and iOS mobile apps; the cross-platform code-sharing approach is still open.

Think about delivery phase as well as audience. During the current phase, develop and test Android phone and tablet experiences, including the TCL 502 (T432J, Android 13) and an emulator. Keep future iPhone support possible by separating activity rules, domain models, and portable data formats from Android UI, storage, and hardware APIs. Prefer libraries with a viable iOS path for code that may be shared, but do not add cross-platform infrastructure before it provides practical value. Before sharing data across apps, define the data format, ownership, authorization, and failure behavior. Before any hosted or institution rollout, design account lifecycle, support, backups, incident response, and operational costs.

## Code-file headers

Add a short descriptive header to every new source-code file. When substantially revising an existing source file that lacks one, add a header if it will help explain the file's role. Use the comment syntax customary for the language and keep the header useful rather than ceremonial.

- State what the file is responsible for and where it fits in the app or subsystem.
- For a standalone tool, script, or executable, add concise usage or launch instructions and any important input/output expectations.
- For one file among many related implementation files, a one- or two-line description is enough; don't repeat instructions that belong in a README.
- Avoid headers that restate the code line by line or become stale when implementation details change.

Example:

```kotlin
/**
 * Stores MindSteps game-session records in the local Room database.
 * Used by game screens to record session and task outcomes.
 */
```

## Build in small, understandable steps

- Prefer a small complete feature over broad scaffolding. Keep architecture proportionate to the current prototype and add layers when they solve a real problem.
- Preserve the working local install/build path. Avoid editing machine-specific SDK configuration such as `android/local.properties` unless specifically needed.
- Keep app boundaries clear. A feature should have an obvious owner; shared data should move through documented, versioned formats rather than accidental database or code coupling.
- Make records from Android phones/tablets, future iPhones, and the desktop web app interoperable through a common, versioned data contract. Each platform may use its own local database; do not design around copying one platform's database file to another. Keep IDs, timestamps, units, activity versions, and profile ownership consistent, and make synchronization safe to retry and resilient to offline periods.
- Explain notable changes in terms of user-visible behavior, data effects, and how they were checked. Do not claim a build or device check that was not performed.
- Do not add or run tests unless the user asks to test or verify the implementation. A requested build/install check is separate and may be performed when appropriate.

## Scale deliberately

- For the current Android prototype, local SQLite/Room and on-device processing are appropriate. Keep platform-specific storage behind a clear boundary where practical, so a future iOS implementation can use its own storage while preserving the same data meanings. Keep data structures understandable and avoid premature cloud services, microservices, distributed databases, or complex dependency injection.
- Preserve stable record identifiers, timestamps, units, activity/form versions, and schema versions where they are needed to interpret data over time or move it later.
- Design repositories, queries, exports, and screens to avoid assuming that only one senior or a handful of sessions will ever exist. Add indexes for real query paths and paginate large histories when those histories become part of the product.
- When a shared service becomes necessary, establish expected caseload, activity volume, retention, transfer size, latency, availability, and cost limits. Measure actual bottlenecks and set capacity alerts before scaling infrastructure.
- Keep records isolated by profile and organization from the start of any multi-user feature. Never rely on a user interface filter as the only access control.

## Privacy and security

Privacy and security are foundational requirements for Mnemosyne and part of dependable, smooth operation. Treat activity history, profile details, caregiver relationships, schedules, location, voice, images, and care notes as sensitive. Apply these practices from the prototype onward:

- Collect and retain only what a feature needs. Prefer local storage and on-device processing when practical; do not transmit data merely because a network service is available.
- Make sharing explicit, visible, limited to named/authorized people, and revocable. Authenticate users and check authorization for every record and operation, especially in caseload and administrative interfaces.
- Use encrypted transport for all network communications between Mnemosyne apps, devices, and services. Treat activity, check-ins, location events, reminders, and voice messages as sensitive, and encrypt sensitive data at rest on devices and services. For live intercom audio, prioritize low latency using the real-time transport’s built-in media protection; do not add a separate application-layer encryption step without evaluating its latency and security tradeoff. Never send traffic over a deliberately unencrypted channel. Use platform security facilities and vetted libraries; never invent cryptography or store credentials in source control.
- Validate untrusted input and treat imports, deep links, network payloads, and uploaded files as untrusted. Use safe database queries, restrict exported components and permissions, and keep dependencies and secrets under control.
- Keep diagnostics free of unnecessary personal details and never log raw audio, images, access tokens, or full care records for convenience. Separate operational diagnostics from user activity history and define retention for both.
- Plan for lost devices, revoked access, account recovery, deletion, backup/restore, and interrupted sync. Make failures visible and preserve local records until a transfer is safely confirmed.
- Consider misuse as well as external attackers: no covert enrollment, hidden monitoring, or silent access changes. Show who has access and provide an understandable way to review or revoke sharing.

## Interface and accessibility

Design MindSteps for a large touch target, readable text, clear contrast, simple navigation, forgiving interactions, and adjustable audio pace/volume. Offer tap alternatives to drag, draw, microphone, or camera tasks when practical. Camera and microphone use should start only for an activity that needs them, be apparent to the player, and avoid retaining raw media by default.

Keep KinBridge usable one-handed on a phone and the desktop product efficient for reviewing multiple profiles without mixing their information. Give each user clear context about which senior/profile they are viewing and the freshness/source of shared information.

## Documentation and data changes

- Keep `README.md` accurate as the repository entry point. Update the relevant guide when setup, app behavior, data fields, or architecture changes.
- Use `Development playbook.md` for agreed project-wide engineering workflow, `brainstorming ideas.md` for possibilities and product direction, and `exercises.md` for the evolving MindSteps activity catalog.
- Document database schema changes and migrations. Preserve existing user records during upgrades; do not delete or reset application data as a shortcut.
- Keep generated build output and machine-specific configuration out of authored documentation and source changes.
