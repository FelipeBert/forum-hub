# Module outputs expose their `value` expression as-is; verify that it is the
# bucket ARN expected by consumers that scope permissions to this bucket.
output "bucket_arn" {
  description = "The ARN of the newly created S3 bucket"
  value       = aws_s3_bucket.artifact-bucket.arn
}