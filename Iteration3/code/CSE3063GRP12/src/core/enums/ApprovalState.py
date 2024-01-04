from enum import Enum

class ApprovalState(Enum):
    APPROVED = "Approved"
    REJECTED = "Rejected"
    PENDING = "Pending"