# AI prompt log: Stage 1

| Number | Model | Exact prompt text | Files changed |
| --- | --- | --- | --- |
| 1 | GitHub Copilot | Implementar el núcleo de Stage 1: fechas tipadas, política de préstamos, excepción común y DAO base. | `db/LocalDatePersister.java`, `service/LoanPolicy.java`, `service/NotFoundException.java`, `dao/BaseDao.java`, modelos y DAOs |
| 2 | GitHub Copilot | Actualizar servicios existentes para LocalDate, LoanPolicy y la guardia de inventario de Stage 1. | `service/BookService.java`, `service/MemberService.java`, `service/ReservationService.java` |
| 3 | GitHub Copilot | Reemplazar el seed original por el seed exacto de Stage 1 y hacerlo idempotente con DAOs compartidos. | `db/Database.java` |
| 4 | GitHub Copilot | Completar el estado de navegador, logging, licencias y actualización Stage 1 del diagrama. | `resources/public/app.js`, `resources/public/index.html`, `resources/logback.xml`, `docs/class-diagram.puml` |
| 5 | Gemini 3.7 Flash | Auditar y pulir Stage 1: completar documentación Javadoc exhaustiva (@param, @return, @throws), limpiar throws SQLException en DAOs y compatibilidad de seed. | `dao/BaseDao.java`, `dao/BookDao.java`, `dao/LoanDao.java`, `dao/MemberDao.java`, `dao/ReservationDao.java`, `model/Loan.java`, `model/Reservation.java`, `model/Book.java`, `model/Member.java`, `service/BookService.java`, `service/MemberService.java`, `service/LoanService.java`, `service/ReservationService.java`, `App.java`, `db/Database.java`, `db/LocalDatePersister.java`, `DueDateDuplicationTest.java`, `TransactionBugTest.java` |