🔍 Log Analyzer

**Log Analyzer** is a tool for analyzing and detecting suspicious patterns in logs, with a focus on **cybersecurity**.
The project is built using **Spring Boot**, **GraalVM Native**, and integrates with **Loki** for log collection and **Prometheus** for metrics exposure.

**🚀 Main Features:**

✅ Detects **common attacks**, such as **Brute Force**, **SQL Injection**, **XSS**, **RCE**, and **Path Traversal**.

✅ **Loki integration**, enabling efficient log analysis in distributed systems.

✅ **Exports metrics** to Prometheus, allowing monitoring via Grafana.

✅ **Optimized with caching and multithreading** for high-performance log processing.

✅ **GraalVM native build**, making the binary faster and more efficient.

# ⚡ Getting Started

**Prerequisites**

- Java 21 (GraalVM)
- Docker (for Loki, Prometheus, and Grafana)
- Gradle

**Required Infrastructure**

This project depends on **log monitoring infrastructure** for containerized services like **Loki**, **Prometheus**, and **Grafana**.

You can find the Docker setup in the following repository:

**🔗 Log Monitoring Infrastructure: https://github.com/adlermedrado/log-monitoring-infra**

Clone and start the infrastructure before running the application.

**Steps to Run**

**1️⃣ Clone this repository**

```bash
git clone https://github.com/seu-usuario/log-analyzer.git
cd log-analyzer
```

**2️⃣ Start Loki, Prometheus, and Grafana:**

```bash
cd ../log-monitoring-infra
docker-compose up -d
```

**3️⃣ Compile the project:**

```bash
./gradlew nativeCompile
```

**4️⃣ Run the application:**

```bash
./build/native/nativeCompile/log-analyzer
```

**📊 Monitoring**

- **Metrics Endpoint:** http://localhost:8080/actuator/prometheus
- **Grafana Dashboard:** http://localhost:3000 (Username: admin / Password: admin)
- **Loki API:** http://localhost:3100/loki/api/v1/query_range

**🔥 Roadmap**

**✅ Phase 1 – Manual Rule-Based Detection**

🔹 Implement security event detectors for common attacks.

🔹 Integrate with Loki for efficient log queries.

🔹 Export security metrics to Prometheus and Grafana.

**🚧 Phase 2 – Optimization & Expansion (In Progress…)**

🔹 Add AI-driven anomaly detection using Ollama and Llama3 models.

🔹 Improve detection with contextual log analysis.

🔹 Expand automated testing for increased reliability.

**🚀 Phase 3 – Future Enhancements (Planned)**

🔹 Support for new log sources and formats.

🔹 Implement alerting via Webhook, Slack, and Telegram.

🔹 Develop an alternative Rust version for higher performance.

---

**⚖️ License**

This project is open-source and distributed under the MIT License.

---

**🎯 Contributions are welcome!**

If you have suggestions, feel free to open an issue or submit a pull request. 🚀
