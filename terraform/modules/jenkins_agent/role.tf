// IAM roles provide temporary credentials to EC2 through an instance profile.
// The trust policy determines which AWS service is allowed to assume the role.
resource "aws_iam_role" "ec2_s3_put_role" {
  name = "ec2-s3-put-role"

  # For an EC2 workload, the service principal is normally
  # ec2.amazonaws.com; verify this value before applying the role.
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_policy" "s3_put_only_policy" {
  name        = "s3-put-only-policy"
  description = "Allows EC2 instances to write/upload objects to a specific S3 bucket"

  # Grant only object uploads, scoped to objects under the bucket ARN.
  # This least-privilege scope avoids granting access to unrelated buckets.
  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "s3:PutObject"
        ]
        Resource = "${var.bucket_arn}/*"
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "attach_s3_policy" {
  # Attach the permissions policy to the role so its sessions can use them.
  role       = aws_iam_role.ec2_s3_put_role.name
  policy_arn = aws_iam_policy.s3_put_only_policy.arn
}

resource "aws_iam_instance_profile" "ec2_s3_profile" {
  # EC2 uses an instance profile to obtain temporary credentials for this role.
  name = "ec2-s3-instance-profile"
  role = aws_iam_role.ec2_s3_put_role.name
}