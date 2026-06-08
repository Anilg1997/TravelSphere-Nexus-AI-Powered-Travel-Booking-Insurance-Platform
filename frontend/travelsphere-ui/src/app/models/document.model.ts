export interface DocumentRequest {
  type: string;
  bookingRef: string;
  format?: string;
}

export interface Document {
  id: string;
  type: string;
  bookingRef: string;
  fileName: string;
  fileUrl?: string;
  createdAt: string;
  status: string;
}
