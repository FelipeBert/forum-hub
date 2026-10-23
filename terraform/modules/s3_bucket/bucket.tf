// Create a uniquely prefixed bucket for build artifacts. S3 bucket names are
// globally unique, so a prefix avoids requiring a fixed name in this module.
resource "aws_s3_bucket" "artifact-bucket" {
  bucket_prefix = "forum-hub-artifacts-"

  tags = {
    Name    = "Artifact Bucket"
    Project = "forum-hub"
  }
}