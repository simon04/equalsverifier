/*
 * Copyright 2009-2011, 2013-2014 Jan Ouwens
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.jqno.equalsverifier.integration.basic_contract;

import static nl.jqno.equalsverifier.testhelpers.Util.assertFailure;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.testhelpers.points.Point;

import org.junit.Test;

public class NonNullityTest {
	@Test
	public void fail_whenNullPointerExceptionIsThrown_givenNullInput() {
		EqualsVerifier<NullPointerExceptionThrower> ev = EqualsVerifier.forClass(NullPointerExceptionThrower.class);
		assertFailure(ev, "Non-nullity: NullPointerException thrown");
	}
	
	@Test
	public void fail_whenEqualsReturnsTrue_givenNullInput() {
		EqualsVerifier<NullReturnsTrue> ev = EqualsVerifier.forClass(NullReturnsTrue.class);
		assertFailure(ev, "Non-nullity: true returned for null value");
	}
	
	@Test
	public void fail_whenEqualsDoesNotTypeCheck() {
		EqualsVerifier<NoTypeCheck> ev = EqualsVerifier.forClass(NoTypeCheck.class);
		assertFailure(ev, ClassCastException.class, "Type-check: equals throws ClassCastException");
	}
	
	@Test
	public void fail_whenEqualsDoesNotTypeCheckAndThrowsAnExceptionOtherThanClassCastException() {
		EqualsVerifier<NoTypeCheckButNoClassCastExceptionEither> ev = EqualsVerifier.forClass(NoTypeCheckButNoClassCastExceptionEither.class);
		assertFailure(ev, IllegalStateException.class, "Type-check: equals throws IllegalStateException");
	}
	
	static final class NullPointerExceptionThrower extends Point {
		public NullPointerExceptionThrower(int x, int y) {
			super(x, y);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (!obj.getClass().equals(getClass())) {
				return false;
			}
			return super.equals(obj);
		}
	}
	
	static final class NullReturnsTrue extends Point {
		public NullReturnsTrue(int x, int y) {
			super(x, y);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return true;
			}
			return super.equals(obj);
		}
	}
	
	static final class NoTypeCheck {
		private int i;
		
		public NoTypeCheck(int i) {
			this.i = i;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj == null) {
				return false;
			}
			return i == ((NoTypeCheck)obj).i;
		}
	}
	
	static final class NoTypeCheckButNoClassCastExceptionEither {
		private int i;
		
		public NoTypeCheckButNoClassCastExceptionEither(int i) {
			this.i = i;
		}
		
		@Override
		public boolean equals(Object obj) {
			try {
				if (obj == null) {
					return false;
				}
				return i == ((NoTypeCheckButNoClassCastExceptionEither)obj).i;
			}
			catch (ClassCastException e) {
				throw new IllegalStateException(e);
			}
		}
	}
}