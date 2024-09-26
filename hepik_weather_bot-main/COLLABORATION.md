# Basic Contribution Guidelines.

- **_Thou shall never commit to the main branch in collaborative project_** ~ Unknown. All changes must be made on a separate branch.
- No local merges to the main branch from your branch. All code must be peer-reviewed before merging.
- Prefer small atomic commits. If the change touched multiple files/code, please add it to the commit description.
- Properly format your code before checking it in.
- If code isn't understandable at first glance, comment it. A single line comment is enough. Add a multi-line comment if necessary.
- All methods and classes must be documented [JavaDocs][javadoc_link] style.
- Minimize unless necessary code duplication.
- All merge requests **MUST** describe the changes that have been introduced.

# Branch Naming convention

**{{ change type }} / {{ your-branch-name }}**

A branch can introduce:
- A new feature
- A fix for a bug
- Documentation
- A test

Let's say your branch name is "my-awesome-branch"

1. For a new feature - **feat/my-awesome-branch**
2. For a fix - **fix/my-awesome-branch**
3. For documentation - **docs/my-awesome-branch**
4. For a test - **test/my-awesome-branch**

# Commit Pattern

- Must start with a verb followed by a description.

``` text

# If adding some method multiplying two numbers

Add method to multiply numbers ✅
Adds method to multiply numbers ❌

```

- Keep the commit title short and concise within the 72-character-limit.
- Commit description should be provided if more information is required. Each line should start with the right-pointing angled bracket `>`.
- To add a description in the shell with `git`: 
  1. Press enter twice before closing the `git commit -m` command
  2. Add description with the start of the line having `>`.


```shell

# The command should look like this in the terminal
git commit -m "Add something

> This is a description
> This is another line" # Close the commit message here if this is the last line

```

- If using an IDE for the commits, skip a line before adding a description.

<!-- External links -->
[javadoc_link]: https://www.oracle.com/ie/technical-resources/articles/java/javadoc-tool.html