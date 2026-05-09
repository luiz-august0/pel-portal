import { UserType } from '@/core/auth/schema';
import 'next-auth';

declare module 'next-auth' {
  interface Session {
    user: UserType;
  }
}
