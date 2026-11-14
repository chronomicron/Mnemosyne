# MindSteps Design Requirements

This is the working design brief for **MindSteps from Mnemosyne**, the tablet app for seniors. Use it to collect requirements and design decisions as we discuss the app. It is intentionally incomplete: ideas under “Open questions” are prompts for discussion, not settled requirements.

For the broader product and engineering principles, see [AGENTS.md](../AGENTS.md) and the [Development Playbook](../Development%20playbook.md). For the catalog of activity ideas, see [exercises.md](../exercises.md).

## Product and audience

- **Suite's primary customer and setup person:** a caregiver, such as an adult child, who may purchase the service, organize setup, and manage the connection to the senior's other tools.
- **Direct MindSteps user:** the senior using the app on a phone or tablet, potentially with reduced eyesight, limited dexterity, or little experience with touch-screen games. The senior-facing experience must remain simple and comfortable even when the caregiver manages setup.
- **Typical setup:** a trusted caregiver helps install and configure MindSteps, set up the resident profile and location preferences in KinBridge, and link the apps. The senior uses MindSteps directly for activities, location support, voice messages, and intercom with the caregiver.
- **Current phase:** design and local prototyping. Decide what the app needs before expanding the existing Android prototype into a larger product.
- **Current implementation:** the early Android prototype is in [`../android/`](../android/) and currently has picture-pair matching and picture-pattern continuation.

## Starting design requirements

These points have been raised as requirements for the tablet experience:

- Use a tablet-sized layout with large, readable text; avoid small labels and dense screens.
- Make buttons and touch targets large and easy to select.
- Keep screens clear and uncluttered, with enough spacing between actions to reduce accidental taps.
- Keep recurring controls—such as Back, Next, Pause, Help, and Undo—in consistent locations, with the same icon, label, and behavior throughout the app.
- Use one familiar visual language across activities so controls do not need to be relearned on each screen.
- Make one action the clear primary action on a screen. Offer optional spoken instructions or replay where useful.
- Avoid time pressure by default. Give calm, clear feedback without alarming sounds or visuals, and do not use color alone to communicate meaning.
- Design activities for touch interaction. Dragging, drawing, microphone, camera, and motion-sensor activities should have an alternate interaction or a skip option where practical.
- Make it easy to pause, repeat instructions, or leave an activity without losing the user's place unexpectedly.
- Make each session feel pleasant and dependable. Avoid avoidable pauses, failures, and interruptions; preserve the senior's place if the app or device is interrupted, and give a calm, clear way to resume when something does go wrong.
- Keep the app useful for relaxed play as well as repeatable activities whose settings and results can be compared over time.
- MindSteps includes more than games: its resident-side feature set includes location sharing/route support, caregiver voice-message playback, and an intercom endpoint. KinBridge provides the caregiver-side setup, monitoring view, alerts, and controls for those shared features.
- Make games enjoyable enough to encourage the senior to return to the tablet. Their second purpose is to create a useful history of play and activity for the caregiver, alongside the senior's own experience.

## Design areas to work through

### Navigation and screen layout

Minimize confusion by keeping the app predictable and familiar. Treat navigation and common controls as a shared design system. Back, Next, Pause, Help/replay, and Undo should stay in predictable positions and retain the same meaning and visual treatment from the home screen through every activity. Prefer the same icon plus a clear text label, especially while people are learning the app. Avoid exposing caregiver setup and data-management controls in the senior's main activity flow unless there is a clear need.

Consistency is a strong design commitment, not a ban on improvement. If observation or testing shows a control is confusing or inaccessible, revise the shared pattern deliberately and update it throughout the app. Avoid changing the location or meaning of a familiar control in just one activity.

### Activity experience

For each activity, define its purpose, instruction wording, practice round, difficulty options, feedback, pause/skip behavior, and the information to record. The [exercise library](../exercises.md) is the source for candidate activities; this brief should capture the decisions about how those activities feel and work in the app.

When the senior chooses a game, show a visually simple selection screen with a small number of large, clearly labeled game choices at a time. Use an easy-to-recognize image or symbol with a short title, avoid a crowded grid or long list, and provide one obvious way to see more games if needed.

### Periodic activity check-in

Offer a caregiver-configured reminder for a short set of activities even when the senior does not otherwise open the games. The caregiver can set a recurring interval of up to three months between prompts, or choose a more frequent schedule. When the senior chooses to begin, MindSteps presents a small, familiar set of about three or four activities. Keep the activities and versions recorded for each session so the caregiver can view changes over time. The senior can pause and resume the set; the reminder should appear only when they are not in the middle of another activity. Save results locally and share them with KinBridge when connected.

### Game enjoyment and preferences

After a game, MindSteps may optionally ask a simple question such as “Did you enjoy this game?” with clear choices such as **Yes**, **It was okay**, and **No**, plus an easy skip. Use the response to offer games the senior enjoys more often, while letting the caregiver adjust the schedule. Store enjoyment as a separate preference signal, never as a performance result. Keep the periodic activity check-in set stable for comparison unless the caregiver deliberately changes it.

### Pause, suspend, and resume

Save game state progressively after every meaningful action, rather than waiting for a pause or session end. Keep the current puzzle, selections, and progress in local storage so the latest checkpoint survives a reminder, app backgrounding, or unexpected shutdown. If the senior leaves an activity, locks or backgrounds the tablet, or stops interacting long enough that they may have stepped away, suspend the session and stop its timer. When MindSteps is opened again, keep the session paused and offer one clear **Continue** action that returns to the latest saved point; do not restart the activity or penalize the pause. The inactivity timeout and behavior after a device restart remain to be chosen.

### Accessibility and comfort

Work out adjustable text size, contrast, sound volume and pace, animation/motion settings, portrait or landscape support, left/right-handed use, and alternatives for touch gestures. Avoid relying on color alone to communicate an instruction or result. Keep timed play optional where it is not essential to the activity.

### Visual style and assets

Choose an approachable visual style that feels calm and respectful rather than childish or clinical. Record the asset needs for each screen: icons, illustrations, photographs, audio prompts, animations, and activity-specific objects. Prefer original or properly licensed assets, and keep important instructions understandable without decorative imagery.

### Tablet and technical setup

- Current development and testing target: Android phones and tablets. Initial phone test device: TCL 502 (T432J), Android 13. Choose and document an Android tablet test device or emulator profile before finalizing dimensions.
- A future iPhone app is part of the product direction. Make technology and data-model decisions with that in mind while keeping Android as the current implementation and test platform. Whether to optimize a future Apple version for iPad is still open.
- Support offline play for core activities and save game history locally.
- Ask for camera or microphone permission only when an activity needs that feature; explain its use in plain language and do not retain raw media by default.
- Consider tilt and other device sensors only for activities that need them, and offer an alternative interaction where possible.
- Keep local data changes migratable so future versions can preserve a person's history.

### Location support, caregiver connection, and intercom

MindSteps itself includes the senior-side GPS/location support, route prompts, caregiver voice-message playback, and intercom endpoint. The carried MindSteps tablet is intended to act both as the senior's activity device and as the location/communication device. KinBridge supplies the caregiver-side setup and monitoring tools: configuring home, familiar places and travel corridors; viewing authorized game statistics and location information; receiving an audible or vibration alert if the senior leaves home or an expected corridor; and sending messages or initiating an intercom request. The carried device must have usable location capability and a connectivity path for remote alerts; verify those requirements on the target Android tablet and phone.

### Tablet activity and caregiver check-ins

MindSteps can provide simple activity signals that help the caregiver know the tablet has been used. For example, it could report that the tablet moved after a quiet period and that a game session began that morning. KinBridge can present the observations together as a concise check-in, such as “Tablet movement detected at 7:43; MindSteps game started at 7:51.” Keep sensor movement, screen interaction, and game play as distinct event types so the caregiver can see what actually happened and interpret it in context.

The detailed GPS, familiar-route, missing-person, voice-message, and intercom concepts are documented in the [shared MindSteps–KinBridge feature brief](../KinBridge/location-and-voice.md), with related exploratory notes in the [brainstorming document](../brainstorming%20ideas.md).

### One-tap caregiver check-in

KinBridge should let a caregiver send a simple “Are you okay?” check-in to MindSteps, either on demand or at a caregiver-configured time. Show a clear, friendly prompt using the senior's preferred name, with large **Yes** and **No** buttons. Send the selected answer and response time to KinBridge so the caregiver can see the result. Keep the interaction short and easy to dismiss or answer; if there is no answer, show “No response yet” rather than treating silence as a Yes. Show when MindSteps last connected and whether a request is pending, delivered, answered, or expired. Separately show the latest tablet interaction received by KinBridge—its event type and timestamp, such as a game starting or a check-in being answered. Label connection time, received activity, and the age of the latest update clearly; an offline device may have newer activity waiting to sync. If the device is offline, expire the request rather than presenting it after it is no longer timely. When the senior device remains offline, KinBridge should make it easy for the caregiver to call the alternate phone number saved for the senior using the ordinary phone network.

### Senior reminders and upcoming plans

MindSteps should deliver simple, caregiver-configured reminders on the senior's device. Support one-time events and repeating schedules for appointments and outings, household routines (such as putting out the garbage on collection night), regular activities (such as going to church on Sunday), and medication prompts at caregiver-entered times (including multiple reminders during the day when scheduled). A reminder can prepare the senior for an upcoming plan by saying what will happen, when it will happen, and who is coming to help—for example: “Good morning, Alice. You have a doctor's appointment this afternoon. Andrew will come to pick you up.” Use the resident's preferred name and familiar wording, with spoken playback and a large, readable on-screen version. KinBridge or the caregiver setup flow manages each schedule and the details shared with MindSteps.

The caregiver chooses the reminder schedule, how far ahead a plan is announced, and whether the senior can hear it again, snooze it, or dismiss it. Medication prompts follow the caregiver-entered schedule and wording, without the app calculating or changing medication instructions.

### Reminders during an activity

Do not interrupt an active game or puzzle with a pop-up, spoken reminder, sound, or control change; the senior may think the activity stopped working or lose focus. Hold reminders until the senior finishes the activity or chooses to pause or exit. Then present queued reminders one at a time in the familiar reminder layout, without losing the activity state. Caregivers should schedule reminders with this non-interrupting behavior in mind.

### Long-range plans and meaningful dates

Support reminders scheduled weeks or months ahead as well as daily and weekly routines. Caregivers can add birthdays, anniversaries, holidays, family visits, and other meaningful dates, choose whether they repeat yearly, and set one or more advance notices—for example, a heads-up several days beforehand and a short reminder on the day. The reminder can include a simple preparation prompt, such as calling a family member or getting ready for a planned visit, when the caregiver has configured it. Keep these dates optional and personalized to the senior.

Daily and weekly routines can include practical household tasks and errands, such as checking the mailbox, putting out or bringing in the garbage bins, watering plants, picking up groceries, or moving laundry. Include pet-care routines such as feeding at caregiver-set times, changing a cat’s litter box on selected days, and walking a dog two or three times a day if that is the usual routine. A caregiver can also schedule household prompts, such as turning the porch light on at dusk or off at bedtime, and checking doors or windows as part of an evening routine. Let the caregiver set the days and preferred times, and use the pet’s name and familiar wording where configured. A reminder is the baseline behavior; automatic light control would require a separately configured compatible smart-home integration. For a planned outing, caregivers can also configure a short “before you go” checklist of items the senior may need, such as glasses, keys, a mobility aid, or a prescribed inhaler. The senior-facing experience should present one useful next step at a time, with an easy way to hear it again or mark it done, skipped, or later.

### Smart offline behavior

Store active schedules, reminder wording, and any needed audio on the MindSteps device so reminders can run without internet or a cloud service. KinBridge changes can update the local schedule when connectivity returns; keep the current schedule usable in the meantime and sync reminder events later without duplicating them. After a device restart, restore upcoming schedules and handle reminders whose time passed during the interruption according to a caregiver-configured grace period, rather than replaying a burst of stale alerts. The tablet needs battery or other power to display or speak a reminder; after a power interruption, resume the stored schedule once the device starts again. Record the scheduled time, actual presentation time, and any response as separate events.

### Illustrative daily-use flow

This is a flexible example, not a required routine. The caregiver configures the schedule and shared information; the senior gets a simple, familiar experience:

1. In the morning, MindSteps can greet the senior and show one clear “what’s next” item, such as an appointment and who is picking them up. Whether the greeting starts automatically or waits for the senior to open the app is still undecided.
2. Before an outing, a short caregiver-configured checklist can remind them to bring items such as glasses, keys, a mobility aid, or a prescribed inhaler.
3. At a time of their choosing, the senior can select a game from a small set of large, clear options. After play, they may say whether they enjoyed it; favorites can be offered more often. A caregiver-scheduled set of three or four activities provides a periodic check-in, even if the senior rarely chooses games.
4. Household, appointment, pet-care, and other reminders use the saved local schedule and appear one at a time when the senior is not in an activity. Game progress saves as they go, and a session can be suspended and resumed later.
5. The caregiver can send a simple Yes/No check-in through KinBridge. KinBridge also shows last connection and the latest interaction it has received, and can help the caregiver call the senior’s alternate phone number if MindSteps remains offline.
6. An evening reminder might cover a configured household routine, such as the porch light or charging the tablet.

### Android and Apple code-sharing options

MindSteps and KinBridge are intended to have separate installable Android and iOS apps; Android phones and tablets are the current development and testing targets, with a future iPhone app planned. Mnemosyne desktop is intended to be a browser-based web app that works across common computer platforms. Building Android and iOS apps with shared code is feasible with current cross-platform tools. The MindSteps Android prototype uses Kotlin and Jetpack Compose, so Kotlin Multiplatform (KMP) is a natural option to evaluate as the app grows. Compose Multiplatform can share UI as well as logic across Android and iOS; KMP can also share game/data logic while keeping an Android Compose UI and a native SwiftUI UI on Apple devices. Flutter is another viable cross-platform route, but adopting it would mean rebuilding the current Kotlin prototype in Dart. The exact mobile code-sharing choice remains open.

For now, avoid tying activity rules, scoring definitions, and portable record formats directly to Android-only APIs. Put Android-specific UI, local storage, permissions, and sensor access at clear boundaries so an iOS implementation can provide equivalent platform-specific pieces later. Keep the architecture simple; this is a portability constraint, not a requirement to migrate to KMP immediately.

### Security for shared information

All communications between MindSteps, KinBridge, the desktop app, and any Mnemosyne service must use encrypted transport. Activity and check-in events, location updates, reminder schedules, and caregiver voice messages are sensitive and must also be encrypted at rest on devices and services. Live intercom audio is a lower confidentiality priority than stored personal records; prioritize low delay using a real-time transport with built-in media protection, and avoid adding a second application-layer encryption step unless its security benefit justifies measured latency costs. Choose and document the end-to-end encryption model for shared records before implementing cloud sync.

### Data shared across apps and devices

MindSteps records need to remain understandable across Android phones and tablets, future iPhones, KinBridge, and the Mnemosyne desktop web app. Use one documented, versioned data contract with consistent meanings for profile IDs, record IDs, timestamps, activity/form versions, results, and units. Each app may keep its own local database implementation; the goal is interoperable records and reliable transfer, not copying a raw database file between Android and Apple devices.

Keep Android session records portable as the prototype grows. When cross-app sharing is designed, choose a transfer path that all clients can use, preserves unsent records while offline, safely handles retries and duplicates, and confirms when a transfer has completed. Whether the first sharing path is manual export/import or an authenticated shared service/API remains undecided.

All approaches still require platform-specific builds and testing. iOS app builds and simulator runs require a Mac with Xcode; this current Linux development machine can continue Android work, but iOS validation will need access to a Mac, whether local or through a suitable build service. Verify accessibility behavior, camera/microphone permissions, and sensor handling on each platform even if most code is shared.

Official starting references: [Kotlin Multiplatform platform stability](https://kotlinlang.org/docs/multiplatform/supported-platforms.html), [Kotlin migration and code-sharing options](https://kotlinlang.org/docs/multiplatform/faq.html), [iOS build setup](https://kotlinlang.org/docs/multiplatform/quickstart.html), and [Flutter supported platforms](https://docs.flutter.dev/reference/supported-platforms).

## Decisions and discussion notes

Add dated decisions here as we settle them. Distinguish a confirmed requirement from a prototype choice that we may revisit.

| Date | Topic | Decision or question | Status |
| --- | --- | --- | --- |
| 2026-11-14 | Readability and touch | Large text and large buttons are core design requirements for the senior-facing tablet app. | Confirmed |
| 2026-11-14 | Consistency | Minimize confusion through predictable flows and familiar controls: keep them in the same locations with consistent icons, labels, and behavior across screens and activities. Change the shared pattern only deliberately when there is a clear usability reason. | Confirmed |
| 2026-11-14 | Pleasant, reliable sessions | Avoid unnecessary interruptions and preserve activity progress so the senior can continue after a device or app interruption. | Confirmed direction |
| 2026-11-14 | Periodic activity check-in | Offer a caregiver-scheduled set of about three or four activities at intervals no longer than three months, with results saved for longitudinal review. | Confirmed direction |
| 2026-11-14 | Game enjoyment | Let the senior optionally rate whether they enjoyed a game; use this to offer preferred games more often, separately from performance results. | Confirmed direction |
| 2026-11-14 | Activity flow | Favor one clear primary action per screen, optional audio help, no default time pressure, and calm feedback that does not rely on color alone. | Confirmed starting direction |
| 2026-11-14 | Mobile platforms | Develop and test Android phone and tablet apps first. Keep future iPhone apps for MindSteps and KinBridge possible. The desktop product should be a browser-based web app. The cross-platform code-sharing approach remains open. | Confirmed direction |
| 2026-11-14 | Shared data | MindSteps, KinBridge, and desktop clients must be able to exchange compatible records across Android and Apple platforms; use a common versioned contract while allowing each platform its own local database. The transfer mechanism remains open; all data in transit and sensitive stored data must be encrypted. | Confirmed direction |
| 2026-11-14 | Customer and direct user | Mnemosyne primarily serves caregivers as the purchasers and setup organizers; seniors remain the direct users of MindSteps on the tablet. MindSteps may also be the senior endpoint for intercom with KinBridge. | Confirmed direction |
| 2026-11-14 | MindSteps purpose | Games should be enjoyable and encourage regular senior interaction with the tablet, while game results and activity events also provide useful history for the caregiver through KinBridge. MindSteps includes resident-side GPS, route support, voice messages, intercom, and simple caregiver check-in prompts. | Confirmed direction |
| 2026-11-14 | Senior reminders | MindSteps should present caregiver-configured, familiar reminders for appointments, household routines, regular activities, and medication schedules, including when plans are happening and who is coming to help. Reminders must be stored and delivered locally while offline, then sync events when possible. | Confirmed direction |

## Open questions

- What tablet size and orientation should define the first design target?
- Should a future Apple MindSteps app also include a fully optimized iPad layout?
- Should we evaluate Kotlin Multiplatform with shared Compose UI, Kotlin Multiplatform with native SwiftUI on Apple, or a Flutter rewrite?
- Should cross-app transfer begin with user-managed export/import or an authenticated shared service/API?
- Should a morning greeting or “what’s next” summary speak automatically at a caregiver-set time, or wait until the senior opens MindSteps?
- Should the home screen lead with a daily activity, a simple activity chooser, or both?
- Which controls should be available on every screen, and where should they sit?
- Which text-size and contrast settings should be adjustable by the senior, caregiver, or both?
- What visual style, colors, typefaces, illustration approach, and audio voice feel welcoming to the intended users?
- Which activity types need touch-only alternatives to drawing, dragging, voice, camera, or motion sensors?
- What information should the senior see after an activity, and what information belongs in caregiver-facing views later?
- How far in advance should MindSteps give reminders, and which response controls should be available by default?
- Which emulator/tablet profile should be used alongside the TCL phone during development?
