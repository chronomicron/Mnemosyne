# Mnemosyne Development Playbook

## Purpose

This document describes how we want to turn Mnemosyne ideas into software: deliberately, in small steps, with the people who will use it in mind. It draws on production engineering themes from the supplied developer talk and adapts them to Mnemosyne's three planned products: **MindSteps** for the senior, **KinBridge** for the primary caregiver, and a **Mnemosyne desktop app** for social workers, nurses, or other care professionals. For coding-agent instructions—including the source-file header convention and practical security/scaling guidance—see [AGENTS.md](AGENTS.md).

This is a working guide, not a fixed architecture or a promise that every brainstormed feature will be built. Record new ideas in [brainstorming ideas](brainstorming%20ideas.md); update this playbook when the project's agreed working methods change.

## Product shape

- **MindSteps from Mnemosyne** runs on the senior's tablet. It offers approachable games and exercises, works offline where practical, and records performance by activity and skill area over time.
- **KinBridge from Mnemosyne** runs on the primary caregiver's phone. It helps the caregiver stay connected, coordinate support, and review the information the senior has chosen or authorized to share.
- **Mnemosyne desktop** supports social workers, nurses, or care teams reviewing an assigned group of seniors, potentially around a dozen people. Each person's information must remain distinct and access must be limited to the worker's assigned caseload.

These are related products with separate user experiences. They should exchange information through documented interfaces and formats, not depend on sharing one large codebase or exposing every record to every app. Each should remain useful on its own.

## Development ethos

### 1. Start with a useful slice

Build the smallest complete feature that lets us learn whether an idea is understandable and useful. Prefer a working activity with saved results over a broad mockup of a dozen unfinished activities. Keep early architecture simple and local-first. Add infrastructure such as cloud services, distributed databases, microservices, or large-scale operations when a concrete use case calls for it.

Think about cost and sudden growth before adding a service that could create an open-ended bill. Set sensible limits and understand what the service charges for. Do not pay the complexity cost of hypothetical scale during a local prototype.

### 2. Design for the actual users and devices

Keep the senior's experience clear, calm, and easy to use on a tablet. Keep the caregiver's tools practical on a phone. Give care professionals a desktop view that can handle several separate resident records efficiently. Make shared features work across these different contexts instead of assuming every user has the same device, role, or technical confidence.

### 3. Treat data as a product interface

Agree on what each measure means before building charts or sharing results. For every record that may leave its originating app, retain enough context to interpret it later, such as:

- A stable resident/profile identifier and a unique record identifier.
- Timestamp, originating app, record type, and session or event identifier.
- Activity definition and version, content/form identifier, skill area, result, and units.
- Interaction mode and relevant conditions, such as playback speed, input method, retries, hints, or interruptions when available.
- Schema version and provenance, so future versions can import old records and tell who or what created them.

Prefer a documented, versioned data contract. Begin with a reliable local export/import path if that meets the need; add automatic synchronization when the user experience justifies it. Make transfers retryable and safe to repeat, and plan how the apps handle duplicates, partial transfers, offline use, and schema upgrades. Never silently discard records when an import or migration fails.

### 4. Build security and privacy into ordinary engineering

Collect only what a feature needs. Keep personal records local when possible, protect data in storage and transfer, authenticate users, and give each role only the access needed for its work. Make pairing and sharing deliberate, visible, and revocable. Keep access to one resident separate from another's, especially in the desktop caseload view.

Operational logs and analytics are not the resident's activity history. Keep them separate, minimize or redact identifying details, control retention, and do not route resident activity data into external analytics by default. A choice to stop sharing must also stop future transmission through logs, analytics, exports, or notifications where relevant.

### 5. Make reliability visible and recoverable

Assume devices go offline, storage fills, transfers are interrupted, and services sometimes fail. Give users a clear outcome and a safe next step. Preserve unsent data locally, retry where appropriate, and show when information was last synchronized. Design important workflows for partial failure, not just the happy path.

Add observability that helps us answer practical questions: did a session save, did a transfer complete, which app version was involved, and where did a failure occur? Use structured events and error reports rather than relying only on scattered console messages. Avoid logging sensitive content or audio merely to make debugging easier.

### 6. Verify the workflows that matter

For each feature, identify the important user journey and what success looks like before implementation. As the system grows, protect critical journeys with appropriate unit, integration, and device-level checks. Give particular attention to saving a game session, preserving timing/results, exporting and importing data, keeping resident profiles separate, and recovering from an interrupted transfer.

For changes, report what was changed and what verification was performed. If verification is not performed, say so plainly. Before broader distribution, establish a release checklist and a repeatable build/install process for the tablet, caregiver phone, and desktop clients.

### 7. Make feedback easy to give and act on

Provide a low-friction way for caregivers and professionals to report confusion, bugs, or useful ideas. Ask for enough information to reproduce a problem—app version, activity, and optional steps or screenshot—without making feedback burdensome or collecting unrelated personal information. Separate feature requests from technical error reports and respect the user's sharing choices.

### 8. Roll out change with a way back

New activities, sync behavior, or reports can behave differently on real devices than they do during development. Where a feature has meaningful user impact, make it possible to keep it limited, enable it selectively, or turn it off without losing the user's existing records. In an offline prototype this may be a local setting or build configuration; remote experiment/feature flags only make sense once there is a service to operate them.

Plan database migrations and data-format changes so older records remain readable. A rollback should not corrupt or erase data created by the newer version.

### 9. Add service-specific obligations when those features arrive

- **Payments:** The current product direction has three tiers: MindSteps remains free with useful games and personal tracking; KinBridge is a paid family/caregiver service; and the desktop caseload product is a paid institution offering. Prices, entitlements, and billing models are still open. Before implementing payments, define what each tier includes, the server/support costs it must cover, account and receipt handling, and how failed payments, renewals, cancellations, and access changes behave. Do not put MindSteps' core games or personal progress history behind the paid tiers.
- **Community or user-posted content:** The core three-app workflow does not require a public community. If sharing, reviews, or user-posted material is introduced, design reporting, moderation, abuse handling, and rate limits as part of that feature rather than as follow-up work.
- **Scale:** Measure actual usage and bottlenecks before choosing scaling techniques. Set cost limits and capacity expectations before enabling any cloud feature that could grow unexpectedly.

## Working workflow

1. **Capture the idea.** Add it to the brainstorm document and identify the intended user, device, and problem it addresses.
2. **Define the slice.** Agree on the first user journey, what data it creates or consumes, and what would count as a useful result.
3. **Check boundaries.** Decide which app owns the feature, which other app (if any) needs its data, and what happens offline or when sharing is disabled.
4. **Choose the simplest workable design.** Note important tradeoffs, dependencies, privacy/security implications, and future migration needs before committing to a larger architecture.
5. **Implement in reviewable steps.** Keep changes focused. Explain file and behavior changes in plain language so the project owner can steer or learn from them.
6. **Verify the critical path.** Build or check the feature and its data behavior on the relevant device or emulator when requested and available; record what was actually verified.
7. **Review and learn.** Gather feedback from the intended users, record what worked and what did not, then update the backlog and this playbook as needed.

## Current development sequence

1. Continue the local Android prototype and learn from installing it on an emulator and the TCL 502 phone.
2. Expand MindSteps activities and record the per-activity results needed for personal trends, including task/version details and timing where relevant.
3. Define and document the shared, versioned data format before multiple apps rely on it.
4. Build a first export/import workflow and a caregiver-facing view in KinBridge or the desktop app.
5. Add more automatic synchronization, team/caseload workflows, and cloud infrastructure when a concrete use case requires them. Implement KinBridge and institution billing once their included features, operating costs, and payment flows are defined; keep MindSteps free.
