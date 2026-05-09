import { portalClient } from "@/core/http/httpClient";
import { RelationshipType } from "@/types/domains/document";

export async function updateInternalRelationshipType(relationshipType: RelationshipType): Promise<void> {
  await portalClient.patch(`/user/update-internal-relationship-type`, undefined, {
    params: { relationshipType }
  });
}