resource "kubernetes_deployment" "players" {
  metadata {
    name = "players"
  }

  depends_on = [
    kubernetes_deployment.postgres
  ]

  spec {
    replicas = 1

    selector {
      match_labels = {
        app = "players"
      }
    }

    template {
      metadata {
        labels = {
          app = "players"
        }
      }

      spec {
        volume {
          name = "players-data-volume"
          persistent_volume_claim {
            claim_name = "players-data"
          }
        }
        init_container {
          name  = "init-sys"
          image = "busybox"
          command = ["sh", "-c", "wget -O /data/players.csv https://raw.githubusercontent.com/rosti-il/players/master/data/players.csv"]
          volume_mount {
            name       = "players-data-volume"
            mount_path = "/data"
          }
        }
        container {
          image = "players:0.0.1-SNAPSHOT"
          name  = "players"

          volume_mount {
            name       = "players-data-volume"
            mount_path = "/data"
          }

          env {
            name  = "SPRING_PROFILES_ACTIVE"
            value = "prod"
          }

          env {
            name  = "SPRING_DATASOURCE_URL"
            value = "jdbc:postgresql://${kubernetes_service.postgres.metadata[0].name}:5432/playersdb"
          }

          env {
            name  = "SPRING_DATASOURCE_USERNAME"
            value = "postgres"
          }

          env {
            name  = "SPRING_DATASOURCE_PASSWORD"
            value = "postgres"
          }

          port {
            container_port = 8080
          }
        }
      }
    }
  }
}

resource "kubernetes_service" "players" {
  metadata {
    name = "players"
  }

  spec {
    selector = {
      app = "players"
    }

    port {
      port        = 8080
      target_port = 8080
    }

    type = "LoadBalancer"
  }
}

resource "kubernetes_persistent_volume_claim" "players_data_pvc" {
  metadata {
    name = "players-data"
  }

  spec {
    access_modes = ["ReadWriteOnce"]
    resources {
      requests = {
        storage = "1Gi"
      }
    }
  }
}
