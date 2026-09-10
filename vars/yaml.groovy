/**
 * Calls the github API and returns a JSON response.
 *
 * @param config A map that contains the config for the Github API:
 *   - `file` (String): The file that's being edited. *Required*.
 *   - `path` (String): The path to the var you're editing *Required*.
 *   - `value` (String): The value of the var you're changing. *Required*.
 */
def editYaml(Map args = [:]) {

    if (!args.file) {
        error("editYaml: 'file' is required")
    }

    if (!args.path) {
        error("editYaml: 'path' is required")
    }

    if (!args.containsKey('value')) {
        error("editYaml: 'value' is required")
    }

    String file = args.file
    String path = args.path
    def newValue = args.value

    if (!fileExists(file)) {
        error("editYaml: YAML file '${file}' does not exist")
    }

    def yaml = readYaml file: file

    List<String> keys = path
        .replaceAll(/\[([0-9]+)\]/, '.$1')
        .split('\\.')
        .findAll { it } as List

    def current = yaml

    keys[0..-2].each { key ->

        if (current instanceof Map) {

            if (!current.containsKey(key)) {
                error("YAML path '${path}' does not exist: missing '${key}'")
            }

            current = current[key]

        } else if (current instanceof List) {

            if (!key.isInteger()) {
                error(
                    "Invalid YAML path '${path}': " +
                    "expected a list index but found '${key}'"
                )
            }

            int index = key.toInteger()

            if (index < 0 || index >= current.size()) {
                error(
                    "Invalid YAML path '${path}': " +
                    "list index '${index}' is out of bounds"
                )
            }

            current = current[index]

        } else {
            error(
                "Invalid YAML path '${path}': " +
                "cannot traverse '${key}' because current value is " +
                "'${current?.getClass()?.simpleName}'"
            )
        }
    }

    // Update final value
    String finalKey = keys[-1]

    if (current instanceof Map) {

        if (!current.containsKey(finalKey)) {
            error(
                "YAML path '${path}' does not exist: " +
                "missing '${finalKey}'"
            )
        }

        current[finalKey] = newValue

    } else if (current instanceof List) {

        if (!finalKey.isInteger()) {
            error(
                "Invalid YAML path '${path}': " +
                "expected a list index but found '${finalKey}'"
            )
        }

        int index = finalKey.toInteger()

        if (index < 0 || index >= current.size()) {
            error(
                "Invalid YAML path '${path}': " +
                "list index '${index}' is out of bounds"
            )
        }

        current[index] = newValue

    } else {
        error(
            "Invalid YAML path '${path}': " +
            "parent is neither a Map nor a List"
        )
    }

    // Overwrite file
    writeYaml file: file, data: yaml, overwrite: true

    echo "Updated YAML: ${yaml}"
    echo "Path: ${path}"
    echo "New value: ${newValue}"
}
