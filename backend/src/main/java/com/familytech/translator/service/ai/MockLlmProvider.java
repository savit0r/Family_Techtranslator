package com.familytech.translator.service.ai;

import com.familytech.translator.dto.AiGenerateRequest;
import com.familytech.translator.dto.AiGenerateResponse;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MockLlmProvider implements LlmProvider {

    @Override
    public AiGenerateResponse generate(AiGenerateRequest request) {
        String prompt = request.prompt() != null ? request.prompt() : "";

        // Check if prompt is an Explain-Back evaluation request
        if (prompt.contains("Explain-Back") || prompt.contains("Target Concept:") || prompt.contains("eval-explain-back")) {
            String evalConcept = extractPattern(prompt, "(?i)Target Concept:\\s*(.*)", "software concept");
            String userExp = extractPattern(prompt, "(?i)Family Member's Explain-Back Answer:\\s*(.*)", "the explanation");

            String evalOutput = String.format("""
                    ## What They Understood
                    - Correctly captured the main purpose of %s using intuitive language.
                    - Demonstrated a clear grasp of how the primary metaphor maps to real-world operations.

                    ## Misunderstandings & Gaps
                    - Minor detail omitted regarding how %s handles extreme edge cases, but no critical misconceptions detected.

                    ## Short Clarification
                    Fantastic job explaining %s in your own words! You've got the core intuition down perfectly.
                    """, evalConcept, evalConcept, evalConcept).trim();

            return new AiGenerateResponse(
                    evalOutput,
                    "mock-model:v1",
                    getProviderName(),
                    100L,
                    true
            );
        }

        String name = extractPattern(prompt, "(?i)- Name:\\s*(.*)", "Family Member");
        String occupation = extractPattern(prompt, "(?i)- Occupation:\\s*(.*)", "daily life");
        String interests = extractPattern(prompt, "(?i)- Hobbies / Interests:\\s*(.*)", "hobbies");
        String concept = extractPattern(prompt, "(?i)concept\\s*\"([^\"]+)\"", "");

        if (concept.isBlank()) {
            concept = extractPattern(prompt, "(?i)- Technical Concept:\\s*(.*)", "Software Concept");
        }

        String analogySummary;
        String conceptBreakdown;
        String techMapping;
        String keyTakeaway;
        String followUpQuestion;

        String conceptLower = concept.toLowerCase();
        String occLower = occupation.toLowerCase();

        if (conceptLower.contains("index")) {
            analogySummary = String.format(
                "For %s, %s is like a categorized index in a %s catalog. Instead of searching page by page, you look up the exact reference key to find the exact location instantly.",
                name, concept, occLower.contains("garden") ? "nursery seed" : occLower.contains("tailor") ? "fabric sample" : "reference"
            );
            conceptBreakdown = String.format(
                "1. Imagine managing thousands of %s items stored in random drawers.\n2. Searching item-by-item requires checking every single drawer from start to finish.\n3. Creating an index builds a mini-directory sorted alphabetically, pointing directly to Drawer #42.",
                occLower.contains("garden") ? "plant species" : occLower.contains("tailor") ? "thread spools" : "records"
            );
            techMapping = String.format(
                "- Catalog Index Key -> %s Indexed Field\n- Drawer Location Pointer -> Database Record Pointer\n- Linear Search -> Table Scan",
                concept
            );
            keyTakeaway = concept + " dramatically speeds up data retrieval by avoiding full table scans.";
            followUpQuestion = String.format("How would you organize an index for %s if you needed to search by multiple properties at once?", name);
        } else if (conceptLower.contains("kuber") || conceptLower.contains("docker") || conceptLower.contains("pod") || conceptLower.contains("container")) {
            analogySummary = String.format(
                "For %s, %s is like an automated %s control system. It packages individual software tasks into standardized units and manages them automatically so work never stops.",
                name, concept, occLower.contains("garden") ? "greenhouse climate & potting" : occLower.contains("tailor") ? "garment assembly line" : "workshop"
            );
            conceptBreakdown = String.format(
                "1. Each container or pod holds a single self-contained application, like a specialized %s station.\n2. %s monitors all stations continuously. If one station gets overloaded or fails, a fresh replacement starts automatically.\n3. This ensures smooth operations even during high demand.",
                occLower.contains("garden") ? "seedling greenhouse" : occLower.contains("tailor") ? "stitching unit" : "workspace",
                concept
            );
            techMapping = String.format(
                "- Isolated Station -> Container / Pod\n- Orchestrator -> %s Engine\n- Auto-healing -> Restarting crashed pods automatically",
                concept
            );
            keyTakeaway = concept + " ensures high availability and automated management of application components.";
            followUpQuestion = String.format("What happens when workload doubles during peak season in %s?", occupation);
        } else if (conceptLower.contains("api") || conceptLower.contains("rest")) {
            analogySummary = String.format(
                "For %s, %s is like a standardized order counter or messenger. It receives specific requests, passes them to the backend system, and delivers the exact response back.",
                name, concept
            );
            conceptBreakdown = String.format(
                "1. A customer or user places a request using a structured menu (API endpoint).\n2. %s translates the request and communicates with the underlying database or service.\n3. The result is packaged neatly and returned to the caller without exposing inner machinery.",
                concept
            );
            techMapping = String.format(
                "- Order Menu -> API Endpoints\n- Request / Response -> HTTP GET / POST Payload\n- Kitchen / Workshop -> Backend Database & Logic"
            );
            keyTakeaway = concept + " allows different systems to communicate safely using standard request formats.";
            followUpQuestion = String.format("How would %s handle invalid or missing information in an order request?", name);
        } else if (conceptLower.contains("recur")) {
            analogySummary = String.format(
                "For %s, %s is like solving a task by breaking it into identical smaller sub-tasks until hitting the simplest base case (like nested boxes or repeated patterns).",
                name, concept
            );
            conceptBreakdown = String.format(
                "1. To complete a complex task, the function calls itself on a smaller piece of the problem.\n2. Each call waits for the smaller sub-problem to finish.\n3. Once the base case is reached, the results combine back up to solve the full task."
            );
            techMapping = String.format(
                "- Base Case -> Stopping condition\n- Recursive Call -> Function invoking itself\n- Stack -> Execution call stack"
            );
            keyTakeaway = concept + " solves big problems by repeatedly applying the same logic to smaller sub-problems.";
            followUpQuestion = String.format("What happens if there is no stopping condition (base case) when using %s?", concept);
        } else if (conceptLower.contains("cache") || conceptLower.contains("memory")) {
            analogySummary = String.format(
                "For %s, %s is like keeping your most frequently used tools right on your workbench instead of walking to the back storage room every time.",
                name, concept
            );
            conceptBreakdown = String.format(
                "1. Fetching data from a main database can take time.\n2. %s stores recent or popular results in fast temporary memory.\n3. Subsequent requests grab data instantly from the workbench cache.",
                concept
            );
            techMapping = String.format(
                "- Quick Access Workbench -> In-Memory Cache\n- Back Storage Warehouse -> Primary Database\n- Cache Miss -> Fetching from storage when not on workbench"
            );
            keyTakeaway = concept + " provides lightning-fast access to frequently requested information.";
            followUpQuestion = String.format("How do you decide when to clear old items off the workbench in %s?", occupation);
        } else {
            analogySummary = String.format(
                "For %s, %s can be understood as a structured, automated helper in %s. It coordinates complex operations so that processes run predictably and efficiently.",
                name, concept, occupation
            );
            conceptBreakdown = String.format(
                "1. %s takes complex technical requirements and breaks them down into predictable operations.\n2. In terms of %s and %s, it organizes resources so nothing is wasted.\n3. Developers use %s to build reliable software solutions.",
                concept, occupation, interests, concept
            );
            techMapping = String.format(
                "- Real-world Metaphor -> %s Core Concept\n- Workflow Inputs -> Request Parameters\n- Final Result -> System Output",
                concept
            );
            keyTakeaway = concept + " simplifies complex workflows into manageable, reliable components.";
            followUpQuestion = String.format("How would you explain the benefits of %s to someone unfamiliar with %s?", concept, occupation);
        }

        String mockOutput = String.format("""
                ## Analogy Summary
                %s

                ## Concept Breakdown
                %s

                ## Analogy-to-Tech Mapping
                %s

                ## Key Takeaway
                %s

                ## Follow-up Question
                %s
                """, analogySummary, conceptBreakdown, techMapping, keyTakeaway, followUpQuestion).trim();

        return new AiGenerateResponse(
                mockOutput,
                "mock-model:v1",
                getProviderName(),
                120L,
                true
        );
    }

    private String extractPattern(String text, String regex, String defaultValue) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String val = matcher.group(1).trim();
            if (!val.isBlank() && !val.contains("{{")) {
                return val;
            }
        }
        return defaultValue;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String getProviderName() {
        return "mock";
    }
}
