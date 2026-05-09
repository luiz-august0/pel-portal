import { HttpError } from "@/core/http/httpError";
import { getCurrentUser } from "@/core/services/auth/getCurrentUserService";
import { login } from "@/core/services/auth/loginService";
import NextAuth, { NextAuthOptions, SessionStrategy } from 'next-auth';
import CredentialsProvider from 'next-auth/providers/credentials';
import { cookies } from 'next/headers';

const nextAuthOptions: NextAuthOptions = {
  providers: [
    CredentialsProvider({
      name: 'credentials',
      credentials: {
        cpf: { label: 'CPF', type: 'text' },
        password: { label: 'Senha', type: 'password' },
        authorizedToken: { label: 'Token', type: 'text' },
      },
      async authorize(credentials) {
        try {
          const loginResponse = await login({
            cpf: credentials?.cpf ?? '',
            password: credentials?.password ?? '',
            authorizedToken: credentials?.authorizedToken ?? '',
          });
          if (loginResponse) {
            const cookieStore = cookies();
            cookieStore.delete('accessToken');
            cookieStore.set('accessToken', loginResponse.accessToken, {
              secure: process.env.NODE_ENV === 'production',
              sameSite: 'lax',
              maxAge: 60 * 60 * 24 * 1, // 1 dia
            });
            const user = await getCurrentUser(loginResponse.accessToken);
            return { ...user, accessToken: loginResponse?.accessToken };
          }

          return null;
        } catch (error) {
          const httpError = error as HttpError;
          throw new Error(JSON.stringify(httpError?.message));
        }
      },
    }),
  ],
  secret: process.env.NEXTAUTH_SECRET,
  session: {
    strategy: 'jwt' as SessionStrategy,
  },
  jwt: {
    secret: process.env.NEXTAUTH_SECRET,
  },
  pages: {
    signIn: "/login",
  },
  callbacks: {
    async jwt({ token, user, session }) {
      const sessionUser = session?.user ?? user;

      return { ...token, ...sessionUser };
    },
    async session({ session, token }) {
      session.user = token as any;
      return session;
    },
  },
  events: {
    async signOut() {
      const cookieStore = cookies();
      cookieStore.delete('accessToken');
    },
  },
};

const handler = NextAuth(nextAuthOptions);

export { handler as GET, handler as POST };
