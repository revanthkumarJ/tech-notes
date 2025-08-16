# Sync Capabilities

This document provides comprehensive information about the synchronization capabilities built into
the KMP Multi-Module Project Generator. These tools help maintain consistency with the upstream
template repository while allowing for project-specific customizations.

## Overview

The project includes a robust synchronization system that allows you to:

- Keep up with template improvements without losing your customizations
- Automatically sync selected directories on a regular schedule
- Apply syncs manually when needed
- Preview changes before applying them

## Sync Directories Script

The `sync-dirs.sh` script is a powerful utility that synchronizes directories and files from the
upstream repository while preserving your local customizations.

### Key Features

- **Comprehensive Sync Coverage**: Syncs the following components:
    - **Applications**: cmp-android, cmp-desktop, cmp-ios, cmp-web
    - **Build System**: build-logic
    - **Tools**: fastlane, scripts
    - **Configuration**: config, .github, .run
    - **Core Files**: Gemfile, Gemfile.lock, ci-prepush scripts

- **Safety System**:
    - Automatic backup of existing files before modification
    - Comprehensive error detection and recovery
    - Progress indication during sync operations
    - Detailed logs of all operations

- **Customization Control**:
    - Preserve specific files and directories from being overwritten
    - Support for exclusions at both directory and root levels
    - Project-specific modifications are maintained

- **Advanced Options**:
    - Dry-run mode to preview changes without applying them
    - Force mode for non-interactive operation in automation
    - Branch creation for review before merging

### Usage

```bash
# Basic sync
./sync-dirs.sh

# Dry run to preview changes
./sync-dirs.sh --dry-run

# Force sync without prompts
./sync-dirs.sh --force

# Both dry run and force mode
./sync-dirs.sh --dry-run --force
```

### Options

| Option        | Description                                         |
|---------------|-----------------------------------------------------|
| `-h, --help`  | Display help information and exit                   |
| `--dry-run`   | Show what would be done without making changes      |
| `-f, --force` | Skip confirmation prompts and proceed automatically |

## GitHub Workflow Integration

The repository includes an enhanced GitHub workflow (`sync-dirs.yml`) that automates the
synchronization process in CI/CD environments.

### Workflow Features

- **Scheduled Execution**: Runs automatically every Monday at midnight UTC
- **Manual Triggering**: Can be triggered manually from GitHub Actions UI
- **Pull Request Generation**: Creates detailed pull requests for review
- **Change Logging**: Includes comprehensive change logs
- **Safety Measures**: Handles all sync components safely
- **Git History**: Maintains proper git history

### Required Workflow Permissions Setup

To use the GitHub workflow effectively, you need to configure the proper permissions:

1. Go to your repository's **Settings**
2. Navigate to **Actions** > **General** in the left sidebar
3. Scroll down to **Workflow permissions**
4. Enable the following permissions:

- ✅ Select "**Read and write permissions**"
- ✅ Check "**Allow GitHub Actions to create and approve pull requests**"

5. Click "**Save**" to apply the changes

## Personal Access Token Setup

To use the `sync-dirs.yml` workflow, you'll need to create a Personal Access Token (PAT) with the
required scopes and save it as a secret.

### Creating a PAT Token

1. Log in to your GitHub account
2. Go to [Developer Settings > Personal Access Tokens](https://github.com/settings/tokens)
3. Click **Generate new token (classic)** or select **Fine-grained tokens**
4. Fill in the following details:

- **Note**: Add a meaningful name like `Sync Workflow Token`
- **Expiration**: Choose an appropriate expiration period
- **Scopes**:
    - ✅ `repo` – Full control of private repositories
    - ✅ `workflow` – To manage and trigger workflows
    - ✅ `write:packages` – To publish and write packages (if applicable)

5. Click **Generate token**
6. Copy the token immediately and save it securely

### Saving the PAT Token as a Secret

#### For a Repository

1. Navigate to the repository where the workflow resides
2. Go to **Settings** > **Secrets and variables** > **Actions**
3. Click **New repository secret**
4. Enter the name `PAT_TOKEN` and paste the token as the value
5. Click **Add secret**

#### For an Organization

1. Navigate to the organization settings
2. Go to **Settings** > **Secrets and variables** > **Actions**
3. Click **New organization secret**
4. Enter the name `PAT_TOKEN` and paste the token as the value
5. Choose the repositories where this secret will be available
6. Click **Add secret**

## Technical Implementation

### Exclusion System

The sync script supports two types of exclusions:

1. **Directory-level exclusions**: Files and directories within specific project directories
2. **Root-level exclusions**: Files in the root of the project

Exclusions are defined in the `EXCLUSIONS` associative array:

```bash
declare -A EXCLUSIONS=(
    ["cmp-android"]="src/main/res:dir dependencies:dir src/main/ic_launcher-playstore.png:file google-services.json:file"
    ["cmp-web"]="src/jsMain/resources:dir src/wasmJsMain/resources:dir"
    ["cmp-desktop"]="icons:dir"
    ["cmp-ios"]="iosApp/Assets.xcassets:dir"
    ["root"]="secrets.env:file"
)
```

### Branching Strategy

The sync process uses a dedicated branching strategy:

1. A new branch is created based on your main development branch
2. A temporary branch is created from the upstream repository
3. Changes are synchronized from the temporary branch to the sync branch
4. The temporary branch is deleted after the sync
5. The sync branch can be pushed for review and merged

## Best Practices for Sync Management

1. **Regular Syncs**: Schedule automated syncs weekly to stay current with upstream changes
2. **Review Changes**: Always review generated PRs carefully before merging
3. **Backup First**: Use `--dry-run` to preview changes before actual sync operations
4. **Conflict Resolution**: Handle merge conflicts promptly to prevent drift
5. **Version Control**: Maintain clean git history during syncs