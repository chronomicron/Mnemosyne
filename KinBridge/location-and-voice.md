# Shared MindSteps–KinBridge Location and Voice Features

This working brief captures the shared location and caregiver-to-senior communication ideas discussed in the Codex chat **“Mnemosyne Vocal.”** These capabilities span **MindSteps** and **KinBridge**; they are not KinBridge-only features. It organizes the discussion for future design and implementation; it does not mean these features are already built or that every detail is final.

## Roles and devices

- **Caregiver:** the primary customer and setup organizer. They use KinBridge on their Android phone first; future iPhone support is part of the product direction.
- **Senior:** uses MindSteps primarily on a carried tablet for games and activities. That same carried device is intended to provide GPS/location support, play caregiver voice messages, and host an announced intercom session; the app also targets Android phones.
- **Care professional:** may later review authorized information in the Mnemosyne desktop web app.

### Feature responsibilities across the two apps

- **MindSteps side:** location capture from the carried resident device, route prompts, game statistics and location records, audible delivery of caregiver messages, and controls to accept, decline, or end an intercom.
- **KinBridge side:** caregiver account and resident profile, setup of home/familiar places/routes and sharing choices, caregiver monitoring view, alerts and history, and sending a voice message or starting an intercom request. The caregiver's phone can buzz or ring when the senior leaves home or a configured route corridor, and can show tablet activity such as movement followed by a game session.
- **Shared layer:** link the authorized caregiver and senior profile/device, exchange versioned location and communication events, and preserve source, time, and status across both apps.

For connectivity-aware check-ins, KinBridge should show the senior device’s last successful connection and the request state (pending, delivered, answered, expired). Show the most recent tablet interaction received by KinBridge separately, including its event type and timestamp—for example, a game session starting or the senior answering a check-in. Label the time of the last data update so caregivers can tell when the tablet may have newer, not-yet-synced events. If MindSteps stays offline, the caregiver should be able to use the senior’s configured alternate phone number for a regular cellular or landline call. Keep that fallback distinct from an in-app voice message or intercom, which needs an internet connection.

MindSteps is responsible for the resident-side GPS and communication capabilities; KinBridge gives the caregiver a way to configure and review them and to receive alerts. The intended setup is for the senior to carry the MindSteps tablet, which acts as both activity interface and location/communication device. MindSteps also targets Android phones. Confirm that the target carried tablet has GPS and a suitable way to transmit location while away from home; location reporting depends on the device being carried, powered, and connected.

## Security requirement for shared information

Use encrypted transport for all communication between MindSteps, KinBridge, and any service. Location reporting, connection and interaction events, caregiver check-ins, reminder updates, and stored caregiver voice messages are sensitive; encrypt them at rest on devices and services. Live intercom speech is a lower confidentiality priority than stored personal records. Prioritize low delay with a real-time transport that provides built-in media protection, and avoid a second application-layer encryption step unless its security benefit justifies measured latency costs. Decide and document the end-to-end encryption model for shared records as part of the service architecture.

## 1. Familiar locations and routes

### Caregiver-configured profile

The caregiver creates a separate profile for each senior they support. Each profile can hold its own:

- Familiar locations, such as home, a nearby supermarket, a particular bank, a friend's home, or a park used for dog walks.
- Usual travel routes or corridors between those locations, including a familiar bus journey.
- Routines and other context that helps interpret whether a location change is expected.

The discussion described a small set of known places covering most of the senior's ordinary outings, rather than treating every coordinate as equally meaningful. Profiles for two parents must remain separate.

### Location monitoring and check-ins

The explored feature uses GPS from the carried MindSteps device. It compares location with caregiver-defined familiar places and routes. Possible events include leaving home, leaving a familiar area, moving well outside an expected corridor, or taking a turn that appears to lead away from a familiar destination.

When a pattern warrants attention, possible responses discussed were:

1. Send an alert to the caregiver's KinBridge phone; it may buzz or ring when the senior leaves home or goes outside a configured corridor.
2. Let the caregiver call or start an announced intercom check-in.
3. Optionally have the senior's device offer a gentle spoken check-in or route prompt, such as asking whether they are heading home and offering a simple direction.

The goal is to help the caregiver notice a possible detour and help the senior get oriented. It should not treat every ordinary deviation as an emergency. The location rules, distance thresholds, route tolerance, prompt timing, and caregiver notification settings are all still open design choices.

### Optional low-detail context signals

The same conversation explored less precise signals that might help a caregiver understand a routine:

- A home Wi-Fi connection disappearing and returning could indicate that the resident's device left and returned home.
- Motion after a quiet period, first screen interaction, or unplugging from overnight charging could contribute to a coarse morning activity timeline.

These are optional device observations, not substitutes for GPS and not proof that a person left home, woke up, or completed an activity. Keep their source and uncertainty visible, and do not merge them into a location claim.

## 2. Missing-person location sharing

For a reported missing-person event, including a Silver Alert scenario discussed in the chat, the idea was to share only a bounded set of recent location information with an appropriately authorized responder. Examples discussed were:

- The latest five or six GPS points, with their timestamps and accuracy information.
- A limited recent window, potentially up to the last 24 hours, when that context is needed.
- Live updates for a limited period if the response requires them.

This emergency view should be scoped to locating the senior. The conversation explicitly distinguished recent location data from the caregiver's fuller profile, routines, and conversation scripts. The authorization flow, eligible responders, time limits, and exact emergency process remain to be designed.

## 3. One-way caregiver voice messages

This is a caregiver-initiated message from KinBridge that plays aloud on the senior's device, like a simple answering machine. It is meant to make it easy to reach someone who may find PINs, unlocking, or a voice-message inbox difficult.

The interaction discussed was:

- The caregiver records or selects a short voice message in KinBridge and sends it to the linked senior device over Wi-Fi or mobile data.
- The senior's device announces who sent it and plays it through the speaker, without requiring the senior to unlock the device, enter a PIN, open an inbox, or accept before playback.
- The resident's microphone stays off during message playback. The senior can stop or dismiss the message.
- After playback, the senior may choose **Accept to talk** to begin a two-way intercom session.

Example message intent: “Hi Mom, it's Andrew. I tried calling and wanted to see how you're doing. Call me back when you can.” The message may also give a simple reminder or ask the senior to call someone.

Sending or playing a message does not show that it was heard. The system may report delivery or playback status when the platform provides it, but should not claim the senior heard or understood it. Playback can be missed if the device is in another room, muted, or otherwise inaudible.

## 4. Announced two-way intercom

The live intercom is a separate, optional step from one-way playback. It was discussed as a short, on-demand way for a caregiver to check in when a normal phone call is not answered or when they want to ask whether the senior needs help.

### Expected flow

1. The caregiver requests an intercom session from KinBridge.
2. The senior's linked device announces who is requesting to talk and makes the active request clear.
3. The senior can accept, decline, or end the conversation.
4. The microphone becomes active only after the senior accepts. Show a clear active-microphone indicator while the session is open.
5. End the session clearly after either person hangs up or after a bounded time. Do not record by default.

The caregiver and senior devices communicate over an internet data connection (Wi-Fi or mobile data); the concept was an in-app intercom rather than an ordinary cellular phone call. Audio should prioritize intelligible speech, low delay, and low data use rather than music-quality fidelity. Mono, speech-oriented compression and adaptive bitrate were discussed as possible implementation choices. Use the real-time transport’s built-in encrypted media protection; avoid adding a separate application-layer encryption layer to live audio unless measurements show it meets the latency target and the added protection is needed.

For a location-triggered route prompt, the user also suggested a call-like ring followed by a gentle spoken question or direction. Treat that as an app prompt, clearly identified as coming from MindSteps, rather than making it seem like a real call from a family member. A caregiver's actual intercom request should identify the caregiver.

If the senior does not answer, the intercom must not silently activate the microphone. The chat specifically raised the desire to check whether a parent might have fallen or need help, but the feasible interaction discussed was an announced request that the senior accepts. Android background microphone limits make a silent remote listen-in an unsuitable assumption. Any alternate emergency flow needs its own design and platform review.

### Separate exploratory idea: ambient listening

The caregiver also wondered whether they could request live ambient audio from the resident's device over Wi-Fi or mobile data to hear what was happening in another room. This is not the same as a caregiver voice message or an accepted intercom: it would be a separate microphone-capture feature, and its purpose, activation, and device/platform feasibility were left unresolved. Keep it clearly separate in future design discussions; it was not settled as a requirement.

## 5. Audio and platform notes

- Keep one-way playback, live intercom, and any future ambient-sound sensing as separate features with distinct permissions and indicators.
- The one-way message does not turn on the senior's microphone. Two-way audio starts only after the senior accepts.
- Keep the intercom brief, announced, and unrecorded by default; it is not continuous or 24-hour audio. Live speech is a lower confidentiality priority than stored location and routine records, so minimize extra processing while retaining the transport’s standard media protection.
- Android is the current development and testing target, across phones and tablets. Keep future iPhone support in mind, and validate background playback, call notifications, audio routing, permissions, and interruption handling on each platform.
- Confirm the GPS accuracy and mobile connectivity available on the carried MindSteps device, with the tablet as the intended primary device and phone support also in scope.

## 6. Data to retain and share

For location or communication events that are saved or shared, retain only what is needed to understand and operate the feature. Candidate event details include:

- Opaque senior/profile ID and unique event ID.
- Event type (location observation, route deviation, caregiver alert, message sent, message delivered/played, intercom requested/accepted/declined/ended).
- MindSteps engagement events, such as first movement after a quiet period, game opened/session started, and game completed. Keep these as separate events with their own timestamps and source.
- Timestamp, source device/app, and schema version.
- For a location observation: coordinates, timestamp, and reported accuracy, with retention and access rules defined separately.
- For a message or intercom: authorized sender/recipient IDs and event status; do not retain audio by default.

Share information only with the caregiver or responder authorized for that senior and event. Keep the common record format portable across Android, future iPhone, and the desktop web app; use each device's local store and a documented transfer/sync layer rather than copying database files between platforms. See the [Development Playbook](../Development%20playbook.md) for the shared-data principles.

## Open decisions

- What GPS and connectivity capabilities should the carried Android tablet have for location alerts while away from home?
- Should KinBridge alerts use vibration, sound, or both, and how should the caregiver set quiet hours or alert thresholds?
- Which locations, corridors, schedules, and route deviations should be configured by the caregiver, and how are changes reviewed?
- What minimum GPS frequency and accuracy are useful without consuming excessive battery or collecting unnecessary history?
- Which events notify the caregiver, and can the senior pause or change location sharing?
- Who can initiate an intercom, what happens when the senior does not answer, and how should call requests fit with quiet hours or accessibility preferences?
- What emergency authorization and time-limited responder access process should the product support?
- What local and remote retention periods apply to precise location history, event summaries, and message delivery records?
