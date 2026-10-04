# AI prompt log: Stage 1

| Number | Model | Exact prompt text | Files changed |
| --- | --- | --- | --- |
| 1 | GitHub Copilot | Implementar el núcleo de Stage 1: fechas tipadas, política de préstamos, excepción común y DAO base. | `db/LocalDatePersister.java`, `service/LoanPolicy.java`, `service/NotFoundException.java`, `dao/BaseDao.java`, modelos y DAOs |
| 2 | GitHub Copilot | Actualizar servicios existentes para LocalDate, LoanPolicy y la guardia de inventario de Stage 1. | `service/BookService.java`, `service/MemberService.java`, `service/ReservationService.java` |
| 3 | GitHub Copilot | Reemplazar el seed original por el seed exacto de Stage 1 y hacerlo idempotente con DAOs compartidos. | `db/Database.java` |
| 4 | GitHub Copilot | Completar el estado de navegador, logging, licencias y actualización Stage 1 del diagrama. | `resources/public/app.js`, `resources/public/index.html`, `resources/logback.xml`, `docs/class-diagram.puml` |