# Date Built-ins for SWRLAPI (Protégé)

Custom SWRL built-ins to extract date components and provide current date values.

Provided built-ins (prefix `date:`):

- `date:year(?year, ?date)` — Extracts the year as xsd:int
- `date:month(?month, ?date)` — Extracts the month (1-12) as xsd:int
- `date:day(?day, ?date)` — Extracts the day of month (1-31) as xsd:int
- `date:nowDate(?date)` — Binds to today's date as xsd:date (YYYY-MM-DD)
- `date:nowYear(?year)` — Binds to current year as xsd:int
- `date:nowMonth(?month)` — Binds to current month as xsd:int
- `date:nowDay(?day)` — Binds to current day of month as xsd:int

Input literals accepted: `xsd:date` or `xsd:dateTime` (date portion is used).

## Build

This project is Maven-based. To build a JAR:

```powershell
# From the project folder
mvn -v
mvn -q clean package
```

The output JAR will be in `target/date-builtins-<version>.jar`.

## Install into Protégé

1. Close Protégé.
2. Copy the built JAR to your Protégé `plugins` folder, e.g.:
   - Windows: `C:\Program Files\Protege-5.x\plugins\`
3. Start Protégé with the SWRLTab plugin enabled.
4. In the SWRLTab, add the prefix:
   - Prefix: `date`
   - Namespace: `http://example.com/swrl/date#`

The library is discovered when Protégé can see the class `org.swrlapi.builtins.date.SWRLBuiltInLibraryImpl` at startup.
If dropping the JAR into `plugins` doesn’t register the built-ins in your Protégé version, copy compiled classes under a prefix folder:

```
C:\Program Files\Protege-5.x\plugins\date\org\swrlapi\builtins\date\SWRLBuiltInLibraryImpl.class
```

Copy from your build output:

```
F:\pluginsProtege\date-builtins\target\classes\org\swrlapi\builtins\date\*
```

## Usage examples (SWRL)

- Extract components:
  - `date:year(?y, ?d) ^ date:month(?m, ?d) ^ date:day(?dy, ?d) -> swrlx:makeOWLThing(?out)`
- Current values:
  - `date:nowYear(?y) ^ date:nowMonth(?m) ^ date:nowDay(?d)`
  - `date:nowDate(?today)`

Bind variables as needed; if already bound, the built-ins verify equality.

## Notes

- Requires Java 11+.
- Tested with SWRLAPI 2.0.x and Protégé 5.6+.
- The `date:` prefix must match the namespace above.

## Publish to GitHub

You can publish both the JAR and the prefix ontology so others can install easily.

### 1) GitHub Releases (distribute the JAR)

1. Create a public GitHub repository and push this project.
2. Build locally: the JAR is at `target/date-builtins-<version>.jar`.
3. Create a Release in GitHub and attach the JAR as an asset.

Optional CI (GitHub Actions) can build and attach the artifact automatically.

### 2) Host the prefix ontology (for easy prefix import)

This repo includes `ont/date-builtins.ttl` with:

- Ontology IRI: `http://example.com/swrl/date`
- Namespace/prefix: `http://example.com/swrl/date#` (maps to `date:`)

Host it using GitHub Pages (Settings ▸ Pages) or any static hosting. Then in Protégé:

- Ontology ▸ Ontology imports ▸ Add by IRI…
- Paste the hosted URL for `ont/date-builtins.ttl`.

After import, the `date:` prefix will be available automatically.

### 3) Align namespace (if you host under your domain)

If you host at a different URL, change the namespace in code and TTL:

- In `org.swrlapi.builtins.date.SWRLBuiltInLibraryImpl`:
  - `private static final String NAMESPACE = "https://<your-domain>/date#";`
- In `ont/date-builtins.ttl` change the `@prefix : <...#>` and ontology IRI.
- Rebuild the JAR and republish.

### Quick install summary for users

1. Download the JAR from Releases and drop it into `Protege-5.x\plugins\`.
2. Import the hosted `date-builtins.ttl` by IRI to get the `date:` prefix.
3. Restart Protégé and use `date:year`, `date:nowYear`, etc.
