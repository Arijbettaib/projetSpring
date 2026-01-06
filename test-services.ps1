# PowerShell Script for Testing Microservices
# Usage: ./test-services.ps1

Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "          MICROSERVICES TESTING - POWERSHELL COMMANDS" -ForegroundColor Cyan
Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""

# Colors
$Success = "Green"
$Error = "Red"
$Info = "Cyan"
$Warning = "Yellow"

# Function to test URL
function Test-ServiceURL {
    param(
        [string]$ServiceName,
        [string]$URL,
        [int]$Port
    )
    
    Write-Host "Testing: $ServiceName" -ForegroundColor $Info
    Write-Host "URL: $URL" -ForegroundColor $Info
    
    try {
        $response = Invoke-WebRequest -Uri $URL -UseBasicParsing -TimeoutSec 5 -ErrorAction Stop
        Write-Host "✅ Status Code: $($response.StatusCode)" -ForegroundColor $Success
        Write-Host "   Response: OK" -ForegroundColor $Success
        return $true
    }
    catch {
        Write-Host "❌ Failed: $($_.Exception.Message)" -ForegroundColor $Error
        return $false
    }
    Write-Host ""
}

# Function to parse Eureka response
function Get-EurekaServices {
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:8761/eureka/apps" -UseBasicParsing
        $xml = [xml]$response.Content
        $apps = $xml.SelectNodes("//application")
        
        Write-Host "Services Registered in Eureka:" -ForegroundColor $Info
        Write-Host ""
        
        foreach ($app in $apps) {
            $name = $app.SelectSingleNode("name").InnerText
            $instance = $app.SelectSingleNode("instance")
            $status = $instance.SelectSingleNode("status").InnerText
            $port = $instance.SelectSingleNode("port").InnerText
            $instanceId = $instance.SelectSingleNode("instanceId").InnerText
            
            $statusColor = if ($status -eq "UP") { $Success } else { $Warning }
            Write-Host "  Service: $name" -ForegroundColor $Info
            Write-Host "    - Instance ID: $instanceId" -ForegroundColor $Info
            Write-Host "    - Status: $status" -ForegroundColor $statusColor
            Write-Host "    - Port: $port" -ForegroundColor $Info
            Write-Host ""
        }
        return $apps.Count
    }
    catch {
        Write-Host "❌ Could not fetch Eureka services: $($_.Exception.Message)" -ForegroundColor $Error
        return 0
    }
}

# Main Tests
Write-Host ""
Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "TEST 1: DISCOVERY SERVICE (EUREKA)" -ForegroundColor Cyan
Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""

$eureka = Test-ServiceURL -ServiceName "Discovery Service" -URL "http://localhost:8761/eureka/apps" -Port 8761
Write-Host ""

if ($eureka) {
    Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
    Write-Host "REGISTERED SERVICES:" -ForegroundColor Cyan
    Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
    Write-Host ""
    $serviceCount = Get-EurekaServices
    Write-Host "Total Services Registered: $serviceCount" -ForegroundColor $Info
    Write-Host ""
}

Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "TEST 2: AUTH SERVICE" -ForegroundColor Cyan
Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""
$auth = Test-ServiceURL -ServiceName "Auth Service" -URL "http://localhost:8080/" -Port 8080
Write-Host ""

Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "TEST 3: PRODUCT SERVICE" -ForegroundColor Cyan
Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""
$product = Test-ServiceURL -ServiceName "Product Service" -URL "http://localhost:9091/" -Port 9091
Write-Host ""

Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "TEST 4: GATEWAY SERVICE" -ForegroundColor Cyan
Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""
$gateway = Test-ServiceURL -ServiceName "Gateway Service" -URL "http://localhost:8888/" -Port 8888
Write-Host ""

# Summary
Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "SUMMARY" -ForegroundColor Cyan
Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host ""
Write-Host "Discovery Service: $(if ($eureka) { '✅ UP' } else { '❌ DOWN' })" -ForegroundColor $(if ($eureka) { $Success } else { $Error })
Write-Host "Auth Service: $(if ($auth) { '✅ UP' } else { '❌ DOWN' })" -ForegroundColor $(if ($auth) { $Success } else { $Error })
Write-Host "Product Service: $(if ($product) { '✅ UP' } else { '❌ DOWN' })" -ForegroundColor $(if ($product) { $Success } else { $Error })
Write-Host "Gateway Service: $(if ($gateway) { '✅ UP' } else { '❌ DOWN' })" -ForegroundColor $(if ($gateway) { $Success } else { $Error })
Write-Host ""

# Java Processes
Write-Host "Running Java Processes:" -ForegroundColor $Info
$javaProcesses = Get-Process java -ErrorAction SilentlyContinue
Write-Host "Total Java Processes: $($javaProcesses.Count)" -ForegroundColor $Info
$javaProcesses | Select-Object Id, ProcessName | Format-Table -AutoSize

Write-Host ""
Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
Write-Host "Testing complete!" -ForegroundColor Cyan
Write-Host "════════════════════════════════════════════════════════════════" -ForegroundColor Cyan
