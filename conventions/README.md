# Conventions

## Coding style

The default style from the IntelliJ IDEA distribution is used, with the following additions and clarifications. 
For complete conventions follow the [link](https://andrey-tsvetkov.youtrack.cloud/articles/TDA-A-1/Conventions).

#### Code files

1. Spaces instead of tabs.
2. Encoding UTF-8\*.
3. Line breaks in Unix-style (LF)\*.
4. Длина строки: рекомендуемая — 140 символов, допустимая — 170.
5. Отсутствие пробелов в конце строки (trailing space).

#### Java

1. Indent 4 spaces, when continuing the expression - 8.
2. Curly braces are required, even for single-line expressions (if/else/do/while/for).
```java
// Wrong
if (condition)
    doSomething();

// Correct
if (condition) {
    doSomething();
}   
```
3. Wildcard imports are prohibited (`import java.util.*`).
4. Imports are grouped (all other, javax, java, all static) and sorted within groups. There must be an empty line between groups.
```java
import ru.crystals.pos.check.ReportShiftEntity;
import ru.crystals.pos.techprocess.TechProcessInterface;

import javax.xml.bind.JAXBException;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
```

### Setting in IntelliJ IDEA

File import: [idea_set_code_style.xml](idea_set_code_style.xml).

1. `File - Settings` (for current project) or `File - Other Settings - Settings for New Projects` (for new).
2. `Editor / Code Style / ⚙️ / Import / IntelliJ IDEA Code Style XML`.
