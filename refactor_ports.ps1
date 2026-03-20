$base = "c:\Users\wilme\Downloads\ConstruccionDeSoftware220261-develop (1)\temp_compare\wilmer-vega\src\main\java\gestiondeunbanco\wilmervega"
cd $base

New-Item -ItemType Directory -Force "domain\ports"
New-Item -ItemType Directory -Force "domain\services"

$companyClientPort = @"
package gestiondeunbanco.wilmervega.domain.ports;

import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import java.util.List;
import java.util.Optional;

public interface CompanyClientPort {
    CompanyClient save(CompanyClient companyClient);
    Optional<CompanyClient> findById(Long id);
    List<CompanyClient> findAll();
    void deleteById(Long id);
}
"@
Set-Content -Path "domain\ports\CompanyClientPort.java" -Value $companyClientPort -Encoding UTF8

$auditLogPort = @"
package gestiondeunbanco.wilmervega.domain.ports;

import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import java.util.List;

public interface AuditLogPort {
    List<AuditLog> findAll();
    AuditLog save(AuditLog auditLog);
    List<AuditLog> findByUserId(Long userId);
    List<AuditLog> findByAffectedProductId(String productId);
}
"@
Set-Content -Path "domain\ports\AuditLogPort.java" -Value $auditLogPort -Encoding UTF8

$companyAdapter = @"
package gestiondeunbanco.wilmervega.repository;

import gestiondeunbanco.wilmervega.domain.models.CompanyClient;
import gestiondeunbanco.wilmervega.domain.ports.CompanyClientPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CompanyClientPersistenceAdapter implements CompanyClientPort {
    private final CompanyClientRepository repository;

    @Override public CompanyClient save(CompanyClient companyClient) { return repository.save(companyClient); }
    @Override public Optional<CompanyClient> findById(Long id) { return repository.findById(id); }
    @Override public List<CompanyClient> findAll() { return repository.findAll(); }
    @Override public void deleteById(Long id) { repository.deleteById(id); }
}
"@
Set-Content -Path "repository\CompanyClientPersistenceAdapter.java" -Value $companyAdapter -Encoding UTF8

$auditAdapter = @"
package gestiondeunbanco.wilmervega.repository;

import gestiondeunbanco.wilmervega.domain.models.AuditLog;
import gestiondeunbanco.wilmervega.domain.ports.AuditLogPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuditLogPersistenceAdapter implements AuditLogPort {
    private final AuditLogRepository repository;

    @Override public List<AuditLog> findAll() { return repository.findAll(); }
    @Override public AuditLog save(AuditLog auditLog) { return repository.save(auditLog); }
    @Override public List<AuditLog> findByUserId(Long userId) { return repository.findByUserId(userId); }
    @Override public List<AuditLog> findByAffectedProductId(String productId) { return repository.findByAffectedProductId(productId); }
}
"@
Set-Content -Path "repository\AuditLogPersistenceAdapter.java" -Value $auditAdapter -Encoding UTF8

# Move services
Move-Item -Force "service\*.java" "domain\services\"
Remove-Item -Force -Recurse "service"

$files = Get-ChildItem -Path . -Recurse -Filter *.java

foreach ($f in $files) {
    if (-not $f.PSIsContainer) {
        $content = Get-Content $f.FullName -Raw
        
        $content = $content -replace "package gestiondeunbanco\.wilmervega\.service;", "package gestiondeunbanco.wilmervega.domain.services;"
        $content = $content -replace "import gestiondeunbanco\.wilmervega\.service\.", "import gestiondeunbanco.wilmervega.domain.services."
        
        if ($f.FullName -match "domain[\\/]services[\\/]") {
            $content = $content -replace "import gestiondeunbanco\.wilmervega\.repository\.", "import gestiondeunbanco.wilmervega.domain.ports."
            $content = $content -replace "CompanyClientRepository", "CompanyClientPort"
            $content = $content -replace "companyClientRepository", "companyClientPort"
            $content = $content -replace "AuditLogRepository", "AuditLogPort"
            $content = $content -replace "auditLogRepository", "auditLogPort"
        }
        
        [System.IO.File]::WriteAllText($f.FullName, $content, (New-Object System.Text.UTF8Encoding $False))
    }
}
Write-Output "Ports and services refactored."
