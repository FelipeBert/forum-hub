# Configures the AWS provider plugin. AWS resources managed by this root module
# are created in the region selected by var.region.
provider "aws" {
  region = var.region
}