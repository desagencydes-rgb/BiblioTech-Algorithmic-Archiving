# Library Management System - Build & Run Script

$ErrorActionPreference = "Stop"

# Paths to Portable Environment
$libDir = Join-Path $PSScriptRoot "lib"
$jdkPath = Join-Path $libDir "jdk"
$javafxPath = Join-Path $libDir "javafx"
$javafxLib = Join-Path $javafxPath "lib"

# Java Binaries
$javac = Join-Path $jdkPath "bin\javac.exe"
$java = Join-Path $jdkPath "bin\java.exe"

# 1. Environment Check
Write-Host "Checking environment..." -ForegroundColor Cyan
if (!(Test-Path $javac) -or !(Test-Path $javafxLib)) {
    Write-Error "Portable environment not found!"
    Write-Host "Please run '.\setup_env.ps1' first to download dependencies." -ForegroundColor Yellow
    exit 1
}

# 2. Cleanup Bin
Write-Host "Cleaning up bin directory..." -ForegroundColor Cyan
if (Test-Path "bin") {
    Remove-Item "bin" -Recurse -Force
}
New-Item -ItemType Directory -Path "bin" | Out-Null

# 3. Compilation
Write-Host "Compiling Java sources..." -ForegroundColor Cyan
try {
    # Get all java files
    $srcPath = Join-Path $PSScriptRoot "src"
    $javaFiles = Get-ChildItem -Path $srcPath -Filter "*.java" -Recurse | Select-Object -ExpandProperty FullName
    
    if (!$javaFiles) {
        Write-Error "No Java source files found in src!"
        exit 1
    }

    # Reference file for long argument list
    $sourcesFile = Join-Path $PSScriptRoot "sources.txt"
    $javaFiles | Out-File $sourcesFile -Encoding ascii

    # Arguments: -d bin -sourcepath src --module-path ... --add-modules ...
    # Note: Using ProcessStartInfo or direct call for better argument handling
    $compileArgs = @(
        "-d", "bin",
        "-sourcepath", "src",
        "--module-path", $javafxLib,
        "--add-modules", "javafx.controls,javafx.fxml",
        "@$sourcesFile"
    )

    $process = Start-Process -FilePath $javac -ArgumentList $compileArgs -Wait -NoNewWindow -PassThru
    
    if ($process.ExitCode -ne 0) {
        Write-Error "Compilation failed with exit code $($process.ExitCode)."
        exit 1
    }
}
catch {
    Write-Error "Compilation error: $_"
    exit 1
}

# 4. Resources
Write-Host "Copying resources..." -ForegroundColor Cyan
$viewsSrc = Join-Path $srcPath "views"
$stylesSrc = Join-Path $srcPath "styles"
$viewsDest = Join-Path "bin" "views"
$stylesDest = Join-Path "bin" "styles"

if (Test-Path $viewsSrc) { Copy-Item -Path $viewsSrc -Destination $viewsDest -Recurse -Force }
if (Test-Path $stylesSrc) { Copy-Item -Path $stylesSrc -Destination $stylesDest -Recurse -Force }

# 5. Run
Write-Host "Running Application..." -ForegroundColor Green
try {
    $runArgs = @(
        "--module-path", $javafxLib,
        "--add-modules", "javafx.controls,javafx.fxml",
        "-cp", "bin",
        "application.Main"
    )
    
    # Run slightly detached or wait? Usually wait for console apps.
    & $java $runArgs
}
catch {
    Write-Error "Runtime error: $_"
}
