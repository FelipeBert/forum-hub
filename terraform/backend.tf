# Use an S3 remote backend so Terraform state is stored centrally rather than
# as a local .tfstate file in the project. State may contain sensitive values,
# so committing it to version control can expose secrets in repository history.
# Limit access to the bucket and enable encryption to protect the state.
# Backend configuration is initialized separately from normal provider configuration.
terraform {
  backend "s3" {
    # S3 bucket that stores the remote state.
    bucket = "terraform-for-study-devops"

    # Object key (path) where this state is stored in the bucket.
    key = "terraform/backend"

    # AWS region where the S3 bucket is located.
    region = "sa-east-1"
  }
}