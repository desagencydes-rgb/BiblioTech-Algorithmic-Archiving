# setup_env.ps1
# Downloads and sets up the portable Java environment

$ErrorActionPreference = "Stop"

$libDir = Join-Path $PSScriptRoot "lib"
$jdkDir = Join-Path $libDir "jdk"
$javafxDir = Join-Path $libDir "javafx"

# URLs
# OpenJDK 21 (Temurin)
$jdkUrl = "https://api.adoptium.net/v3/binary/latest/21/ga/windows/x64/jdk/hotspot/normal/eclipse?project=jdk"
# JavaFX 21.0.1 (Gluon)
$javafxUrl = "https://download2.gluonhq.com/openjfx/21.0.1/openjfx-21.0.1_windows-x64_bin-sdk.zip"

# Create lib directory
if (!(Test-Path $libDir)) {
    New-Item -ItemType Directory -Path $libDir | Out-Null
    Write-Host "Created lib directory."
}

# Download and Extract JDK if not present
if (!(Test-Path $jdkDir)) {
    Write-Host "Downloading OpenJDK 21... (This may take a moment)"
    $jdkZip = Join-Path $libDir "jdk.zip"
    Invoke-WebRequest -Uri $jdkUrl -OutFile $jdkZip
    
    Write-Host "Extracting JDK..."
    Expand-Archive -Path $jdkZip -DestinationPath $libDir -Force
    
    # Rename the extracted folder (it usually has a versioned name) to 'jdk'
    $extractedJdk = Get-ChildItem -Path $libDir -Directory | Where-Object { $_.Name -like "jdk-21*" } | Select-Object -First 1
    if ($extractedJdk) {
        Rename-Item -Path $extractedJdk.FullName -NewName "jdk"
    }
    
    Remove-Item $jdkZip
    Write-Host "JDK installed to $jdkDir"
}
else {
    Write-Host "JDK already exists."
}

# Download and Extract JavaFX if not present
if (!(Test-Path $javafxDir)) {
    Write-Host "Downloading JavaFX 21... (This may take a moment)"
    $javafxZip = Join-Path $libDir "javafx.zip"
    Invoke-WebRequest -Uri $javafxUrl -OutFile $javafxZip
    
    Write-Host "Extracting JavaFX..."
    Expand-Archive -Path $javafxZip -DestinationPath $libDir -Force
    
    # Rename extracted folder (javafx-sdk-21.0.1) to 'javafx'
    $extractedFx = Get-ChildItem -Path $libDir -Directory | Where-Object { $_.Name -like "javafx-sdk-*" } | Select-Object -First 1
    if ($extractedFx) {
        Rename-Item -Path $extractedFx.FullName -NewName "javafx"
    }
    
    Remove-Item $javafxZip
    Write-Host "JavaFX installed to $javafxDir"
}
else {
    Write-Host "JavaFX already exists."
}

Write-Host "Environment setup complete."
